package io.dourl.mqtt.base;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/24
 */
public class BaseApp {
    @SuppressLint("StaticFieldLeak")
    private static Context sApp;

    public static Context getApp() {
        return sApp;
    }

    public static void init(Context application) {
        sApp = application;
    }
}
