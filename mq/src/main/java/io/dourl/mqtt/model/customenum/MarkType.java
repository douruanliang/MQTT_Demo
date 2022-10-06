package io.dourl.mqtt.model.customenum;

import android.graphics.Color;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dourl on 2017/3/12.
 */

public enum MarkType {
    @SerializedName("-99")
    NONE(-99),
    @SerializedName("0")
    COMMON(0),
    @SerializedName("1")
    RECOMMED(1),
    @SerializedName("2")
    HOT(2),
    @SerializedName("3")
    ACTIVE(3),
    @SerializedName("4")
    NEW(4);

    public int value;

    MarkType(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

  /*  public int getStr() {
        int str = R.string.common;
        switch (value) {
            case 0:
                str = R.string.common;
                break;
            case 1:
                str = R.string.recommend;
                break;
            case 2:
                str = R.string.hot;
                break;
            case 3:
                str = R.string.active;
                break;
            case 4:
                str = R.string.newest;

        }
        return str;
    }

    public int getColor() {
        int color = R.color.color_66;
        switch (value) {
            case 0:
                color = R.color.color_66;
                break;
            case 1:
                color = R.color.color_6937ff;
                break;
            case 2:
                color = R.color.color_fa5a7a;
                break;
            case 3:
                color = R.color.color_fe8a41;
                break;
            case 4:
                color = R.color.color_0aca83;

        }
        return color;
    }*/

    public static MarkType valueOf(int i) {
        switch (i) {
            case 0:
                return COMMON;
            case 1:
                return RECOMMED;
            case 2:
                return HOT;
            case 3:
                return ACTIVE;
            case 4:
                return NEW;
            default:
                return NONE;

        }
    }
}
