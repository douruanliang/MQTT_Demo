package io.dourl.mqtt.manager;

import io.dourl.mqtt.storage.SessionManager;
import io.dourl.mqtt.utils.MessageThreadPool;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.job.msg.SendMsgJob;
import io.dourl.mqtt.model.BaseUser;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.model.message.chat.AudioBody;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.EmptyEmojiBody;
import io.dourl.mqtt.model.message.chat.ImageBody;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.model.message.chat.RedPacketBody;
import io.dourl.mqtt.model.message.chat.RedPkgStatus;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.model.message.chat.VideoBody;
import io.dourl.mqtt.utils.IMTextBodyUtils;

/**
 * 消息管理类
 */
@SuppressWarnings("UnusedReturnValue")
public class MessageManager {

    private static MessageManager ourInstance = new MessageManager();

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        if (ourInstance == null) {
            synchronized (SessionManager.class) {
                if (ourInstance == null) {
                    ourInstance = new MessageManager();
                }
            }
        }
        return ourInstance;
    }
/*
    public MessageModel reSend(MessageModel messageModel) {
        MsJobManager.getInstance().addJob(new ReSendMsgJob(messageModel.getMsgId()));
        return messageModel;
    }*/

    /**
     *
     * @param baseUser 给谁发
     * @param content
     * @return
     */
    public MessageModel sendTextMessage(UserModel baseUser, String content) {
        MessageModel textMessage = new MessageModel();
        textMessage.setTo(baseUser);
        TextBody body = new TextBody();
        List<TextBody.TextEntity> textEntities = IMTextBodyUtils.createTextBody(content);
        body.setContent(textEntities);
        body.createSpan(BaseApp.getApp());
        setupNormalProperty(textMessage, body);
        sendMessage(textMessage);
        return textMessage;
    }

    private void sendMessage(MessageModel message) {
        MessageThreadPool.INSTANCE.sendMessage(new SendMsgJob(message));
    }

    public MessageModel sendImageMessage(UserModel baseUser, String imagePath) {
        MessageModel imageMessage = new MessageModel();
        imageMessage.setLocalPath(imagePath);
        imageMessage.setTo(baseUser);
        ImageBody body = new ImageBody();
        ImageBody.ContentEntity contentEntity = new ImageBody.ContentEntity();
        body.setLocalPath(imagePath);
        body.setContent(contentEntity);
        setupNormalProperty(imageMessage, body);
        sendMessage(imageMessage);
        return imageMessage;
    }

    public MessageModel sendAudioMessage(UserModel baseUser, String audioPath, int duration) {
        MessageModel audioMessage = new MessageModel();
        audioMessage.setLocalPath(audioPath);
        audioMessage.setTo(baseUser);
        AudioBody body = new AudioBody();
        body.setLocalPath(audioPath);
        AudioBody.ContentEntity contentEntity = new AudioBody.ContentEntity();
        contentEntity.setL(duration);
        body.setContent(contentEntity);
        setupNormalProperty(audioMessage, body);
        sendMessage(audioMessage);
        return audioMessage;
    }

    public MessageModel sendVideoMessage(UserModel baseUser, String videoPath) {
        MessageModel videoMessage = new MessageModel();
        videoMessage.setLocalPath(videoPath);
        videoMessage.setTo(baseUser);
        VideoBody body = new VideoBody();
        body.setLocalPath(videoPath);
        VideoBody.ContentEntity contentEntity = new VideoBody.ContentEntity();
        body.setContent(contentEntity);
        setupNormalProperty(videoMessage, body);
        sendMessage(videoMessage);
        return videoMessage;
    }

    /**
     * 空文字表情
     */
    public MessageModel sendTextEmptyMessage(UserModel baseUser) {
        MessageModel textMessage = new MessageModel();
        textMessage.setTo(baseUser);
        EmptyEmojiBody body = new EmptyEmojiBody();
        EmptyEmojiBody.ContentEntity contentEntity = new EmptyEmojiBody.ContentEntity();
        contentEntity.setBall((int) (Math.random() * 7));
        contentEntity.setSmile((int) (Math.random() * 7));
        body.setContent(contentEntity);
        setupNormalProperty(textMessage, body);
        sendMessage(textMessage);
        return textMessage;
    }

    public MessageModel saveRedPkgMessage(UserModel baseUser, RedPacketBody.ContentEntity content) {
        MessageModel message = new MessageModel();
        message.setTo(baseUser);
        RedPacketBody body = new RedPacketBody();
        List<RedPacketBody.ContentEntity> redPkgEntities = new ArrayList<>();
        redPkgEntities.add(content);
        body.setContent(redPkgEntities);
        setupNormalProperty(message, body);
        message.setMsgId("red_pkg" + content.getPacket_id());
        message.setSendStatus(MessageModel.Status.success);
        //MessageThreadPool.INSTANCE.sendMessage(new SaveRedPkgMsgJob(message));
        return message;
    }

    public MessageModel saveRedPkgMessage(ClanModel clanModel, RedPacketBody.ContentEntity content) {
        MessageModel message = new MessageModel();
        message.setClan(clanModel);
        RedPacketBody body = new RedPacketBody();
        List<RedPacketBody.ContentEntity> redPkgEntities = new ArrayList<>();
        redPkgEntities.add(content);
        body.setContent(redPkgEntities);
        setupNormalProperty(message, body);
        message.setMsgId("red_pkg" + content.getPacket_id());
        message.setSendStatus(MessageModel.Status.success);
        //MessageThreadPool.INSTANCE.sendMessage(new SaveRedPkgMsgJob(message));
        return message;
    }

    /**
     * 群聊
     */
    public MessageModel sendTextMessage(ClanModel clanModel, String content, List<BaseUser> mAtUsers) {
        MessageModel textMessage = new MessageModel();
        textMessage.setClan(clanModel);
        TextBody body = new TextBody();
        List<TextBody.TextEntity> textEntities = IMTextBodyUtils.createTextBody(content, mAtUsers);
        body.setContent(textEntities);
        List<BaseMsgBody.UserEntity> extraUsers = IMTextBodyUtils.getAtExtra(content, mAtUsers);
        if (extraUsers.size() > 0) {
            BaseMsgBody.ExtraEntity entity = new BaseMsgBody.ExtraEntity();
            entity.uids = extraUsers;
            body.setExtra(entity);
        }
        setupNormalProperty(textMessage, body);
        sendMessage(textMessage);
        return textMessage;
    }

    public MessageModel sendTextEmptyMessage(ClanModel clanModel) {
        MessageModel textMessage = new MessageModel();
        textMessage.setClan(clanModel);
        EmptyEmojiBody body = new EmptyEmojiBody();
        EmptyEmojiBody.ContentEntity contentEntity = new EmptyEmojiBody.ContentEntity();
        contentEntity.setBall((int) (Math.random() * 7));
        contentEntity.setSmile((int) (Math.random() * 7));
        body.setContent(contentEntity);
        setupNormalProperty(textMessage, body);
        sendMessage(textMessage);
        return textMessage;
    }

    public MessageModel sendImageMessage(ClanModel clanModel, String imagePath) {
        MessageModel imageMessage = new MessageModel();
        imageMessage.setLocalPath(imagePath);
        imageMessage.setClan(clanModel);
        ImageBody body = new ImageBody();
        ImageBody.ContentEntity contentEntity = new ImageBody.ContentEntity();
        body.setLocalPath(imagePath);
        body.setContent(contentEntity);
        setupNormalProperty(imageMessage, body);
        sendMessage(imageMessage);
        return imageMessage;
    }

    public MessageModel sendAudioMessage(ClanModel clanModel, String audioPath, int duration) {
        MessageModel audioMessage = new MessageModel();
        audioMessage.setLocalPath(audioPath);
        audioMessage.setClan(clanModel);
        AudioBody body = new AudioBody();
        body.setLocalPath(audioPath);
        AudioBody.ContentEntity contentEntity = new AudioBody.ContentEntity();
        contentEntity.setL(duration);
        body.setContent(contentEntity);
        setupNormalProperty(audioMessage, body);
        sendMessage(audioMessage);
        return audioMessage;
    }

    public MessageModel sendVideoMessage(ClanModel clanModel, String videoPath) {
        MessageModel videoMessage = new MessageModel();
        videoMessage.setLocalPath(videoPath);
        videoMessage.setClan(clanModel);
        VideoBody body = new VideoBody();
        body.setLocalPath(videoPath);
        VideoBody.ContentEntity contentEntity = new VideoBody.ContentEntity();
        body.setContent(contentEntity);
        setupNormalProperty(videoMessage, body);
        sendMessage(videoMessage);
        return videoMessage;
    }

    private void setupNormalProperty(MessageModel message, BaseMsgBody body) {
        if (message.getClan() == null) {
            message.setType(MessageType.CHAT_NORMAL);
            message.setSessionId("u" + message.getTo().getUid());
            message.setToUid(message.getTo().getUid());
        } else {
            message.setType(MessageType.CHAT_GROUP);
            message.setSessionId(message.getClan().id);
            message.setToUid(message.getClan().id);
        }
        message.setMsgId(getMsgUUID());
        message.setFromUid(LoginManager.getInstance().getCurrentUserId());
        message.setFrom(LoginManager.getInstance().getCurrentUser());
        message.setBody(body);
        message.setIsMine(true);
        message.setIsRead(true);
        message.setLocalTime(System.currentTimeMillis());
        message.setSendStatus(MessageModel.Status.sending);
    }

    public String getMsgUUID() {
        return UUID.randomUUID().toString();
    }

    /*public void setMsgRead(final MessageModel msg) {
        MessageDao.setMsgRead(msg.getMsgId());
    }

    public void deleteMsg(final MessageModel msg) {
        MessageDao.deleteMsg(msg);
    }

    public void deleteAllMsg(final CallbackRunnable.Callback callback) {
        DaoHandlerThread.getInstance().execute(new CallbackRunnable(callback) {
            @Override
            public void run() {
                try {
                    SessionDao.deleteAllSession();
                    MessageDao.deleteAllMsg();
                    callback.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e);
                }
            }
        });
    }*/

    public void updateRedPkgStatus(final String msgId, RedPkgStatus status) {
        /*DaoHandlerThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                MessageModel msg = null;
                try {
                    msg = MessageDao.getMsgById(msgId).get();
                    RedPacketBody boy = (RedPacketBody) msg.getBody();
                    RedPacketBody.ContentEntity content = boy.getContent().get(0);
                    if (content.getState() != status) {
                        content.setState(status);
                        MessageDao.insertOrUpdate(msg);
                        msg.setBody(boy);
                        EventBusManager.getInstance().post(new MsgStatusUpdateEvent(msg));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });*/
    }

}
