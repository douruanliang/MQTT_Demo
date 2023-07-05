package io.dourl.mqtt.model.customenum;

import com.google.gson.annotations.SerializedName;

import io.dourl.mqtt.base.BaseObject;


public enum ManagerType implements BaseObject {
    @SerializedName("-1")
    NONE(-1),
    @SerializedName("0")//普通
            normal(0),
    @SerializedName("1")//群主
            leader(1),
    @SerializedName("2")//管理员
            manager(2);

    public int value;

    public String getStringValue() {
        return String.valueOf(value);
    }

    ManagerType(int value) {
        this.value = value;
    }

    public static ManagerType valueof(int value) {
        switch (value) {
            case 0:
                return normal;
            case 1:
                return leader;
            case 2:
                return manager;
            default:
                return NONE;
        }
    }

}
