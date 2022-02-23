package io.dourl.mqtt.model.message.chat;

import com.google.gson.annotations.SerializedName;

/**
 * 红包状态
 * Created by zhangheng on 2018/8/23.
 */

public enum RedPkgStatus {
    @SerializedName("-1")
    UN_KNOWN(-1),
    @SerializedName("0")
    UNOPEN(0),
    @SerializedName("1")
    OPENED(1),
    @SerializedName("2")
    EXPIRE(2),
    @SerializedName("3")
    FINISHED(3);

    private int value;

    RedPkgStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static RedPkgStatus valueOf(int i) {
        switch (i) {
            case 0:
                return UNOPEN;
            case 1:
                return OPENED;
            case 2:
                return EXPIRE;
            case 3:
                return FINISHED;

        }
        return UN_KNOWN;
    }
}
