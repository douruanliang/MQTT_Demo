package io.dourl.mqtt.bean;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.MessageType;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/4/25
 */
public class PublicMessage implements BaseObject {
    private MessageType type;
    private BaseMsgBody body;

    public PublicMessage(MessageType type, BaseMsgBody body) {
        this.type = type;
        this.body = body;
    }
}
