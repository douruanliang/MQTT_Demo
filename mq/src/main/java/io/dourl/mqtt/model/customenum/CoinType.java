package io.dourl.mqtt.model.customenum;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import io.dourl.mqtt.base.BaseObject;

/**
 * Created by zhangpeng on 2017/11/1.
 */

public interface CoinType extends BaseObject {

    public static final int NONE = -1;
    public static final int BTC = 0;
    public static final int ETH = 1;
    public static final int GTC = 2;
    public static final int TRX = 3;
    public static final int GT = 11;
    public static final int USDT = 201;

    public static String getStr(int value) {
        String str = "";
        switch (value) {
            case 0:
                str = "BTC";
                break;
            case 1:
                str = "ETH";
                break;
            case 2:
                str = "GTC";
                break;
            case 3:
                str = "TRX";
                break;
            case 11:
                str = "GT";
                break;
            case 201:
                str = "USDT";
                break;

        }
        return str;
    }


    public static int valueOfName(@NonNull String coinName) {
        if (coinName.equalsIgnoreCase("BTC")) {
            return BTC;
        } else if (coinName.equalsIgnoreCase("ETH")) {
            return ETH;
        } else if (coinName.equalsIgnoreCase("GTC")) {
            return GTC;
        } else if (coinName.equalsIgnoreCase("TRX")) {
            return TRX;
        } else if (coinName.equalsIgnoreCase("GT")) {
            return GT;
        } else if (coinName.equalsIgnoreCase("USDT")) {
            return USDT;
        }
        return NONE;
    }

}
