package io.dourl.mqtt;

import android.app.Application;

import io.dourl.mqtt.base.BaseApp;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/25
 */
public class MQTTApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.init(this);
    }
}
