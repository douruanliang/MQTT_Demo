package io.dourl.mqtt.model.message;

import android.text.TextUtils;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.bean.MessageModel;
import io.dourl.mqtt.manager.GsonManager;
import io.dourl.mqtt.model.message.chat.MessageType;

/**
 * 用来解析基本消息类型
 */
public class ReceiveMessage implements BaseObject {
    /**
     * 消息类型，普通消息，通知等等
     * 具体的消息类型，请关注body中的type
     */
    protected MessageType type;
    protected String fromUid;
    protected byte[] data;
    protected String msg;
    protected String channel;
    protected int version;

    public MessageType getType() {
        return type;
    }

    public String getFromUid() {
        return fromUid;
    }

  /*  public ReceiveMessage(MessageType type, BaseMsgBody body, UserModel from, ClanModel clan) {
        this.type = type;
        this.body = body;
        this.from = from;
        this.fromUid = from.getUid();
        this.clan = clan;
        this.clan_id = clan.id;
    }*/

    public ReceiveMessage(MessageType type,String fromUid, MessageModel data) {
        this.type = type;
        this.fromUid = fromUid;
        this.msg = covertMsgToString(data);
    }

    protected String covertMsgToString(MessageModel data) {
        return android.util.Base64.encodeToString(GsonManager.getGson().toJson(data).getBytes(), android.util.Base64.NO_WRAP);
    }


    protected byte[] covertMsgToData() {
        if (!TextUtils.isEmpty(this.msg)) {
            return data = android.util.Base64.decode(this.msg, android.util.Base64.NO_WRAP);
        }
        return null;
    }

    /**
     * json String
     * @return
     */
    public String getMsg(){
        return new String(covertMsgToData());
    }
}
