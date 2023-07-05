package io.dourl.mqtt.utils;

import android.content.Context;

import androidx.annotation.DimenRes;

import io.dourl.mqtt.base.MqttBaseApp;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/23
 */
public class AppContextUtil {
    public static Context getContext() {
        return MqttBaseApp.getApp();
    }

    /**
     * dip转px
     */
    public static int dip2px(float dpValue) {
        final float scale = MqttBaseApp.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = MqttBaseApp.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getDimen(@DimenRes int resId) {
        return MqttBaseApp.getApp().getResources().getDimensionPixelSize(resId);
    }
}
