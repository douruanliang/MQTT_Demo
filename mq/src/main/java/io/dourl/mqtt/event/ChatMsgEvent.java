package io.dourl.mqtt.event;

import io.dourl.mqtt.bean.MessageModel;

/**
 * 聊天类消息事件
 * Created by zhangpeng on 16/1/7.
 */
public class ChatMsgEvent {

    private String mSessionId;

    private MessageModel message;

    public ChatMsgEvent(String mSessionId, MessageModel message) {
        this.mSessionId = mSessionId;
        this.message = message;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String mSessionId) {
        this.mSessionId = mSessionId;
    }

    public MessageModel getMessage() {
        return message;
    }

    public void setMessage(MessageModel message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMsgEvent{" +
                "mSessionId='" + mSessionId + '\'' +
                ", message=" + message +
                '}';
    }
}
