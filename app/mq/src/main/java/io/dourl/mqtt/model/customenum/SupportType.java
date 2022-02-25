package io.dourl.mqtt.model.customenum;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangheng on 2018/3/14.
 */

public enum SupportType {
    @SerializedName("0")
    ONLY_FREE(0),
    @SerializedName("1")
    ONLY_CHARGE(1),
    @SerializedName("2")
    ALL(2),
    @SerializedName("3")
    NONE(3);

    public int value;

    SupportType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public static SupportType valueOf(int i) {
        switch (i) {
            case 0:
                return ONLY_FREE;
            case 1:
                return ONLY_CHARGE;
            case 2:
                return ALL;
            case 3:
                return NONE;
            default:
                return ONLY_FREE;

        }
    }
}
