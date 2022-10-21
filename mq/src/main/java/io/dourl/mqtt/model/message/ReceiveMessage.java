package io.dourl.mqtt.model.message;

import com.google.gson.annotations.JsonAdapter;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.bean.UserModel;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.model.typeadapter.BodyTypeAdapter;

/**
 * 用来解析基本消息类型
 */
public class ReceiveMessage implements BaseObject {
    /**
     * 消息类型，普通消息，通知等等
     * 具体的消息类型，请关注body中的type
     */
    protected MessageType type;
    /**
     * id : 10042
     * fullname : 哥
     * sessionIcon : http://7xo8e9.com2.z0.glb.qiniucdn.com/avatar/2ddeafaeadc29828b9c77ef0e3281abe.jpg
     * sex : 1
     * truthful : 0
     * age : 25
     * intro : hello
     */

    @JsonAdapter(BodyTypeAdapter.class)
    protected BaseMsgBody body;


    protected UserModel from;
    protected String fromUid;
    protected ClanModel clan;
    protected String clan_id;

    public MessageType getType() {
        return type;
    }

    public BodyType getBodyType() {
        if (body != null) {
            return body.getType();
        }
        return BodyType.UN_RECOGNIZE;
    }

    public String getFromUid() {
        return fromUid;
    }

    public ReceiveMessage(MessageType type, BaseMsgBody body, String uid, String clan_id) {
        this.type = type;
        this.body = body;
        this.fromUid = uid;
        this.clan_id = clan_id;
    }

    public ReceiveMessage(MessageType type, BaseMsgBody body,UserModel from, ClanModel clan) {
        this.type = type;
        this.body = body;
        this.from = from;
        this.fromUid = from.getUid();
        this.clan = clan;
        this.clan_id = clan.id;
    }
}
