package io.dourl.mqtt;

import android.app.Application;
import android.content.Context;

import io.dourl.http.HttpApiBase;
import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.constants.Constants;
import io.dourl.mqtt.utils.DeviceInfoUtils;

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
        HttpApiBase.init(this);
        //TODO
        Constants.SCREENWIDTH = DeviceInfoUtils.getScreenWidth(this);
    }
}
