package io.dourl.mqtt.model.message.chat;

import com.google.gson.annotations.SerializedName;

/**
 * 消息类型
 * Created by zhangpeng on 16/2/29.
 */
public enum MessageType {
    @SerializedName("0")
    UN_RECOGNIZE(0),
    @SerializedName("1")
    CHAT_NORMAL(1),
    @SerializedName("2")
    CHAT_GROUP(2);

    private int value = 0;

    MessageType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static MessageType valueOf(Integer i) {
        if (i == null) {
            return UN_RECOGNIZE;
        }
        switch (i) {
            case 1:
                return CHAT_NORMAL;
            case 2:
                return CHAT_GROUP;
//            case 4:
//                return TRANSACTION;
//            case 5:
//                return ADD_FRIEND;
//            case 6:
//                return EVENT;
            default:
                return UN_RECOGNIZE;
        }
    }
}
