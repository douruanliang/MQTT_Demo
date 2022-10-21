package io.dourl.mqtt.event;


import io.dourl.mqtt.bean.MessageModel;

/**
 * Created by dourl on 16/1/11.
 */
public class MsgStatusUpdateEvent {

    private MessageModel msg;

    public MsgStatusUpdateEvent(MessageModel msg) {
        this.msg = msg;
    }

    public MessageModel getMessage() {
        return msg;
    }

    public void setMsg(MessageModel msg) {
        this.msg = msg;
    }
}
