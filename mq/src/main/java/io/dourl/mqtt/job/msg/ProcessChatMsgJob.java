package io.dourl.mqtt.job.msg;

import android.text.TextUtils;

import com.google.gson.Gson;

import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.SessionModel;
import io.dourl.mqtt.event.ChatMsgEvent;
import io.dourl.mqtt.event.SessionEvent;
import io.dourl.mqtt.manager.EventBusManager;
import io.dourl.mqtt.manager.GsonManager;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.manager.MessageManager;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.model.message.chat.RedPacketBody;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.storage.DbCallback;
import io.dourl.mqtt.storage.MessageDao;
import io.dourl.mqtt.storage.SessionDao;
import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.storage.UserDao;
import io.dourl.mqtt.utils.log.LoggerUtil;

/**
 * 处理聊天消息
 */
public class ProcessChatMsgJob extends BaseMessageJob {

    private static final String TAG = "ProcessChatMsgJob";

    private String mMsgString;
    protected MessageModel mMessageModel;

    public ProcessChatMsgJob(String msg) {
        this.mMsgString = msg;
    }

    @Override
    public void run() {
        if (mMessageModel == null && !TextUtils.isEmpty(mMsgString)) {
            mMessageModel = GsonManager.getGson().fromJson(mMsgString, MessageModel.class);
        }
        if (mMessageModel != null) {
            makeReceivedMessage(mMessageModel);
        } else {
            return;
        }
        if (mMessageModel.getBodyType() == BodyType.TYPE_TEXT) {
            TextBody body = (TextBody) mMessageModel.getBody();
            body.createSpan(MqttBaseApp.getApp());
        } else if (mMessageModel.getBodyType() == BodyType.TYPE_IMAGE) {
        } else if (mMessageModel.getBodyType() == BodyType.TYPE_AUDIO) {
            mMessageModel.setDownloading(true);
           /* MediaDownloadUtils.getAudio(((AudioBody) mMessageModel.getBody()).getContent().getR(),
                    true,
                    mMessageModel.getSessionId(), new DownloadCallback() {
                        @Override
                        public void onSuccess(File baseData) {
                            mMessageModel.setLocalPath(baseData.getAbsolutePath());
                            mMessageModel.setDownloading(false);
                            EventBusManager.getInstance().post(new MsgStatusUpdateEvent(mMessageModel));
                            MessageDao.insertOrUpdate(mMessageModel);
                        }

                        @Override
                        public boolean onFail(int statusCode, File failDate, Throwable error) {
                            mMessageModel.setDownloading(false);
                            EventBusManager.getInstance().post(new MsgStatusUpdateEvent(mMessageModel));
                            MessageDao.insertOrUpdate(mMessageModel);
                            return false;
                        }
                    });*/
        } else if (mMessageModel.getBodyType() == BodyType.TYPE_VIDEO) {
            // FrescoHelper.prefetchToDiskCache(((VideoBody) mMessageModel.getBody()).getContent().getCover_r());
        } else if (mMessageModel.getBodyType() == BodyType.TYPE_A_IMAGE) {
            //FrescoHelper.prefetchToDiskCache(((AImageBody) mMessageModel.getBody()).getContent().getItem_url());
        }
        if (mMessageModel != null) {
            try {
                saveMsgAndPostEvent(mMessageModel);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理接受到的消息,配置相关属性值
     *
     * @param msg
     * @return
     */
    protected MessageModel makeReceivedMessage(MessageModel msg) {
        /*if (msg.getBodyType() == BodyType.TYPE_RED_PACKET_COLLECT) {
            RedPacketOpenBody.ContentEntity contentEntity = ((RedPacketOpenBody) msg.getBody()).getContent().get(0);
            msg.setFromUid(contentEntity.getFrom_uid());
        } else {
            msg.setFromUid(msg.getFromUser().getUid());
        }*/
        msg.setSendStatus(MessageModel.Status.success);
        msg.setTo(LoginManager.getInstance().getCurrentUser());
        msg.setToUid(LoginManager.getInstance().getCurrentUserId());
        msg.setLocalTime(System.currentTimeMillis());
        msg.setMsgId(MessageManager.getInstance().getMsgUUID());
        if (!TextUtils.isEmpty(msg.getFromUid()) && msg.getFromUid().equals(msg.getToUid())) {
            msg.setIsMine(true);
        } else {
            msg.setIsMine(false);
        }

        if (msg.getType().value() == MessageType.CHAT_NORMAL.value()) {
            msg.setSessionId("u" + msg.getFromUid());
        } else {
            msg.setSessionId(msg.getClan_id());
        }
        return msg;
    }

    protected void saveMsgAndPostEvent(MessageModel msg) throws InterruptedException {

        LoggerUtil.d(TAG, GsonManager.getGson().toJson(msg));
        if (msg.getBodyType() == BodyType.TYPE_GROUP_APPLY_NUM) {
            EventBusManager.getInstance().post(new ChatMsgEvent(msg.getSessionId(), msg));
            return;
        }
        //用户入库
        UserDao.insertOrUpdate(msg.getFromUser()).await();
        //消息入库
        if (msg.getBodyType() == BodyType.TYPE_RED_PACKET) {
            RedPacketBody.ContentEntity contentEntity = ((RedPacketBody) msg.getBody()).getContent().get(0);
            msg.setMsgId("red_pkg" + contentEntity.getPacket_id());
            MessageDao.insertOrUpdate(msg).await();
        } else {
            MessageDao.insert(msg).await();
        }
        //更新Session库
        SessionDao.findSessionById(msg.getSessionId(), new DbCallback<SessionModel>() {
            @Override
            public void onSuccess(SessionModel sessionModel) {
                if (sessionModel == null) {
                    sessionModel = SessionManager.createChatSession(msg.getFromUser(), msg);
                }
                sessionModel.setMsgDbId(msg.getId());
                sessionModel.setSessionMsg(msg);
                sessionModel.setUnreadMsgCount(sessionModel.getUnreadMsgCount() + 1);
                sessionModel.setCreateTime(msg.getLocalTime());

                //@提示
           /*     if (msg.getType().value() == MessageType.CHAT_GROUP.value()) {
                    msg.getClan().hasAt = sessionModel.getClan().hasAt;
                    sessionModel.setClan(msg.getClan());
                    if (msg.getBody().getExtra() != null && msg.getBody().getExtra().uids != null)
                        for (BaseMsgBody.UserEntity user : msg.getBody().getExtra().uids) {
                            if (user.uid.equals(LoginManager.getInstance().getCurrentUserId())) {
                                sessionModel.getClan().hasAt = true;
                            }
                        }
                }*/

                try {
                    SessionDao.insertOrUpdate(sessionModel).await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//        SessionManager.getInstance().incrementAndGetUnreadCount();
                EventBusManager.getInstance().post(new SessionEvent(sessionModel));
                EventBusManager.getInstance().post(new ChatMsgEvent(sessionModel.getSessionID(), msg));
            }

            @Override
            public void onFail(Throwable e) {

            }
        }).await();


    }
}
