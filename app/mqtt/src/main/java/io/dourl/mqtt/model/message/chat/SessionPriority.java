package io.dourl.mqtt.model.message.chat;

import com.google.gson.annotations.SerializedName;

/**
 * session优先级，列表排序使用
 * Created by zhangheng on 2018/4/20.
 */

public enum SessionPriority {
    @SerializedName("10")
    MSG_TOP(10),
    @SerializedName("20")
    CHAT_NORMAL(20);

    SessionPriority(int i) {
        this.value = i;
    }

    private int value = 20;

    public int value() {
        return this.value;
    }

    public static SessionPriority valueOf(Integer i) {
        if (i == null) {
            return CHAT_NORMAL;
        }
        switch (i) {
            case 10:
                return MSG_TOP;
            case 20:
                return CHAT_NORMAL;
            default:
                return CHAT_NORMAL;
        }
    }
}
