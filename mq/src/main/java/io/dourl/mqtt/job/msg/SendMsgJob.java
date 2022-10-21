package io.dourl.mqtt.job.msg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import io.dourl.http.api.SendMsgApis;
import io.dourl.http.model.BaseResponse;
import io.dourl.http.retrofit.RetrofitManager;
import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.utils.log.LoggerUtil;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.constants.Constants;
import io.dourl.mqtt.event.ChatMsgEvent;
import io.dourl.mqtt.event.MsgStatusUpdateEvent;
import io.dourl.mqtt.event.SessionEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.model.message.chat.AudioBody;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.HintBody;
import io.dourl.mqtt.model.message.chat.ImageBody;
import io.dourl.mqtt.model.message.chat.VideoBody;
import io.dourl.mqtt.storage.MessageDao;
import io.dourl.mqtt.storage.SessionDao;
import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.storage.UserDao;
import io.dourl.mqtt.utils.ImageUtils;
import io.dourl.mqtt.utils.MediaFilesUtils;
import okhttp3.internal.http2.ErrorCode;
import retrofit2.Response;


/**
 * 消息发送job
 */
public class SendMsgJob extends BaseMessageJob {

    private String TAG = "SendMsgJob";
    protected MessageModel mMessageModel;
    protected SessionModel mSession;

    protected SendMsgJob() {
    }

    public SendMsgJob(MessageModel messageModel) {
        mMessageModel = messageModel;
    }

    @Override
    public void run() {
        try {
            prepare();
            dbOp();
            EventBusManager.getInstance().post(new SessionEvent(mSession));
            doUpload();
            dbOp();
            doSend();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.e(TAG,e.getMessage());
            mMessageModel.setSendStatus(MessageModel.Status.fail);
            updateMessageAndSession();
        }

    }

    private void prepare() throws Exception {
        LoggerUtil.d(TAG, "prepare");
        //插入消息数据表
        MessageDao.insertOrUpdate(mMessageModel).await();
        //创建会话 - 谁有一条带有信息的会话
        mSession = SessionManager.createChatSession(mMessageModel.getTo(), mMessageModel);
        SessionDao.insertOrUpdate(mSession).await();

        switch (mMessageModel.getType()) {
            case CHAT_NORMAL:
                UserDao.insertOrUpdate(LoginManager.getInstance().getCurrentUser()).await();
                UserDao.insertOrUpdate(mMessageModel.getTo()).await();
                break;
            case CHAT_GROUP:
               // UserDao.insertOrUpdate(LoginManager.getInstance().getCurrentUser()).await();
                //UserDao.insertOrUpdate(mMessageModel.getTo()).await();
                break;
        }
        TAG += mMessageModel.getId();
        LoggerUtil.d(TAG,"---job add %s"+ mMessageModel.toString());
        File file;
        if (mMessageModel.getLocalPath() != null) {
            file = new File(mMessageModel.getLocalPath());
        } else {
            notifyNew();
            return;
        }
        switch (mMessageModel.getBodyType()) {
            case TYPE_IMAGE: {//图片需要压缩
                File imageFile = MediaFilesUtils.getSessionImageFile(BaseApp.getApp(), mMessageModel.getSessionId());
                mMessageModel.setLocalPath(ImageUtils.scaleImageFile(BaseApp.getApp(), file, imageFile,
                        Constants.IMAGE_SCALE_SIZE, Constants.IMAGE_SCALE_SIZE).getPath());
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mMessageModel.getLocalPath(), options);
                ImageBody imageBody = (ImageBody) mMessageModel.getBody();
                ImageBody.ContentEntity contentEntity = imageBody.getContent();
                contentEntity.setH(options.outHeight);
                contentEntity.setW(options.outWidth);
                mMessageModel.setBody(imageBody);
                notifyNew();
            }
            break;
            case TYPE_VIDEO: {//视频需要上传封面
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(file.getPath());
                int w = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int h = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                int d = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
                int r = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                Bitmap bm = retriever.getFrameAtTime(0);
                retriever.release();
                File coverFile = MediaFilesUtils.getSessionImageFile(BaseApp.getApp(), mMessageModel.getSessionId());
                ImageUtils.writeBitmapToFile(bm, Bitmap.CompressFormat.JPEG, 80, coverFile);
                VideoBody videoBody = (VideoBody) mMessageModel.getBody();
                videoBody.setCoverPath(coverFile.getPath());
                videoBody.getContent().setL(d);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                videoBody.getContent().setW(bm.getWidth());
                videoBody.getContent().setH(bm.getHeight());
                bm.recycle();
                notifyNew();
                LoggerUtil.d(TAG,"upload cover %s", coverFile.getName());
                String coverKey = "doFileUpload(coverFile, new UpLoadParam(UploadType.image))";
                videoBody.getContent().setCover_r(coverKey);
                File f = doTranscode(file);
                mMessageModel.setLocalPath(f.getPath());
                break;
            }
            default:
                notifyNew();
        }
    }

    protected boolean needUpload() {
        String key = null;
        switch (mMessageModel.getBodyType()) {
            case TYPE_IMAGE: {
                ImageBody imageBody = (ImageBody) mMessageModel.getBody();
                ImageBody.ContentEntity contentEntity = imageBody.getContent();
                key = contentEntity.getR();
            }
            break;
            case TYPE_A_IMAGE:
                break;
            case TYPE_AUDIO: {
                AudioBody audioBody = (AudioBody) mMessageModel.getBody();
                AudioBody.ContentEntity contentEntity = audioBody.getContent();
                key = contentEntity.getR();
            }
            break;
            case TYPE_VIDEO: {
                VideoBody videoBody = (VideoBody) mMessageModel.getBody();
                VideoBody.ContentEntity contentEntity = videoBody.getContent();
                key = contentEntity.getR();
            }
            break;
        }
        return TextUtils.isEmpty(key) && mMessageModel.getLocalPath() != null;
    }

    protected void doUpload() throws Exception {
        if (!needUpload()) {
            return;
        }
        LoggerUtil.d("do Upload");
       /* String key;
        UpLoadParam param = new UpLoadParam(UploadType.image);
        File file = new File(mMessageModel.getLocalPath());
        switch (mMessageModel.getBodyType()) {
            case TYPE_IMAGE:
                param = new UpLoadParam(UploadType.image);
                break;
            case TYPE_A_IMAGE:
                break;
            case TYPE_AUDIO:
                param = new UpLoadParam(UploadType.audio);
                break;
            case TYPE_VIDEO: {//视频需要上传封面
                param = new UpLoadParam(UploadType.video);
            }
            break;
        }
        key = doFileUpload(file, param);
        if (!TextUtils.isEmpty(key)) {
            switch (mMessageModel.getBodyType()) {
                case TYPE_IMAGE: {
                    ImageBody imageBody = (ImageBody) mMessageModel.getBody();
                    ImageBody.ContentEntity contentEntity = imageBody.getContent();
                    contentEntity.setR(key);
                }
                break;
                case TYPE_A_IMAGE:
                    break;
                case TYPE_AUDIO: {
                    AudioBody audioBody = (AudioBody) mMessageModel.getBody();
                    AudioBody.ContentEntity contentEntity = audioBody.getContent();
                    contentEntity.setR(key);
                    mMessageModel.setBody(audioBody);
                }
                break;
                case TYPE_VIDEO: {
                    VideoBody videoBody = (VideoBody) mMessageModel.getBody();
                    VideoBody.ContentEntity contentEntity = videoBody.getContent();
                    contentEntity.setR(key);
                    mMessageModel.setBody(videoBody);
                }
                break;
            }

        }*/
        LoggerUtil.d("upload success, so do send");
    }

    /*protected String doFileUpload(final File file, UpLoadParam p) throws Exception {
        if (file == null || !file.exists()) {
            throw new NullPointerException("file not found");
        }
        LoggerUtil.d("file size %s", FileUtils.showFileSize(file.length()));
        final String[] result = new String[1];
        *//*UploadManager.getInstance().uploadFile(file, p, new UploadCallback() {
            @Override
            public void onSuccess(String key) {
                result[0] = key;
            }

            @Override
            public void onFail(@Nullable Exception e) {

            }

            @Override
            public void onProgress(double p) {
                LoggerUtil.tag(TAG).d("upload progress %s, %s", file.getName(), String.valueOf(p));
            }
        }).waitForCompletion();
        return result[0];
    }*/

    protected void doSend() throws IOException {
        LoggerUtil.d(TAG,"do Send");
        SendMsgApis sendMsgApis = RetrofitManager.get().create(SendMsgApis.class);
        Response<BaseResponse> response = null;
        switch (mMessageModel.getType()) {
            case UN_RECOGNIZE:
                break;
            case CHAT_NORMAL:
                response = sendMsgApis.sendMsg(mMessageModel.getToUid(), mMessageModel.getType().value(), mMessageModel.getPushBody()).execute();
                break;
            case CHAT_GROUP:
                response = sendMsgApis.sendGroupMsg(mMessageModel.getClan().id, mMessageModel.getType().value(), mMessageModel.getGPushBody()).execute();
                break;
        }
        if (response != null) {
            if (response.isSuccessful() && response.body() != null) {
                BaseResponse body = response.body();
                if (body.isSucceeded()) {
                    LoggerUtil.d(TAG,"send success");
                    mMessageModel.setSendStatus(MessageModel.Status.success);
                } else {
                    LoggerUtil.d(TAG,"send fail error: %s", body.getErrorMessage() != null ? body.getErrorMessage() : "");
                    processErrorCode(body.getErrorCode());
                    mMessageModel.setSendStatus(MessageModel.Status.fail);
                }
            } else {
                LoggerUtil.d(TAG,"send fail error: %s", response.message() != null ? response.message() : "");
                mMessageModel.setSendStatus(MessageModel.Status.fail);
            }
            updateMessageAndSession();
        }

    }

    protected void notifyNew() {
        EventBusManager.getInstance().post(new ChatMsgEvent(mMessageModel.getSessionId(), mMessageModel));
    }

    protected void updateMessageAndSession() {
        dbOp();
        EventBusManager.getInstance().post(new MsgStatusUpdateEvent(mMessageModel));
        EventBusManager.getInstance().post(new SessionEvent(mSession));
    }

    protected void dbOp() {
        try {
            SessionDao.updateSendStatus(mSession.getSessionID(), mMessageModel.getSendStatus());
            MessageDao.insertOrUpdate(mMessageModel).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected File doTranscode(final File file) {
        File resultFile = file;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getPath());
        /*int bitrate;
        int w = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int h = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int d = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        retriever.release();
        LoggerUtil.tag(TAG).d("prepare trancode, file size is %s, w: %d, h: %d, bitrate is %d. duration is %d",
                FileUtils.showFileSize(file.length()), w, h, bitrate, d);
        final File transcodeFile = CacheFileUtils.generateTempVideoFilePath(AppConstant.getApp());
        Future future;
        try {
            future = MediaTranscoder.getInstance().transcodeVideo(file.getPath(), transcodeFile.getPath(),
                    new AndroidBitrateFormatStrategy(bitrate), new MediaTranscoder.Listener() {
                        @Override
                        public void onTranscodeProgress(double progress) {
                            LoggerUtil.tag(TAG).d("transcode progress %s", String.valueOf(progress));
                        }

                        @Override
                        public void onTranscodeCompleted() {
                            LoggerUtil.tag(TAG).d("video transcode completed, filesize: %skb", String.valueOf(transcodeFile.length() / 1024));
                        }

                        @Override
                        public void onTranscodeCanceled() {
                            LoggerUtil.tag(TAG).d("onTranscodeCanceled");
                        }

                        @Override
                        public void onTranscodeFailed(Exception exception) {
                            LoggerUtil.tag(TAG).e("onTranscodeFailed", exception);
                            BugTagsUtils.sendException(exception);
                        }
                    });
            future.get();
            if (transcodeFile.exists() && transcodeFile.length() > 0) {
                try {
                    MediaMetadataRetriever r = new MediaMetadataRetriever();
                    r.setDataSource(transcodeFile.getPath());
                    int dw = Integer.parseInt(r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int dh = Integer.parseInt(r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    int dbitrate = Integer.parseInt(r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
                    r.release();
                    LoggerUtil.tag(TAG).d("result video w: %d, h: %d, bitrate: %d", dw, dh, dbitrate);
                    retriever.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    BugTagsUtils.sendException(e);
                }
                resultFile = transcodeFile;
            }
        } catch (Exception e) {
            LoggerUtil.tag(TAG).d("video transcode fail: %s", e.getMessage());
            e.printStackTrace();
        }*/
        return resultFile;
    }

    protected void processErrorCode(int errorCode) {
        switch (ErrorCode.valueOf(String.valueOf(errorCode))) {
            default:
                addErrorMessage();
                break;
        }
    }

    protected void addErrorMessage() {
        MessageModel errorModel = null;
        try {
            errorModel = mMessageModel.clone();
            errorModel.setId(null);
            errorModel.setMsgId(UUID.randomUUID().toString());
            errorModel.setLocalTime(System.currentTimeMillis());
            HintBody baseMsgBody = new HintBody();
            baseMsgBody.setType(BodyType.TYPE_CHAT_REJECT);
            errorModel.setBody(baseMsgBody);
            errorModel.setShowTime(false);
            /*EventBusManager.getInstance().post(new ChatMsgEvent(errorModel.getSessionId(), errorModel));
            MessageDao.insertOrUpdate(errorModel)*/
            ;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
