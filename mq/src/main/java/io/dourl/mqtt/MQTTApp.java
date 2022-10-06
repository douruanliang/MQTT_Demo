package io.dourl.mqtt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.dourl.http.HttpApiBase;
import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.base.log.LoggerUtil;
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
        Constants.SCREENWIDTH = DeviceInfoUtils.getScreenWidth(this);


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int activityStartCount = 0;
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                activityStartCount++;
                LoggerUtil.d("app","onActivityStarted"+activityStartCount);
                if (activityStartCount == 1) {
                    LoggerUtil.d("app","从后台切到前台");
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                activityStartCount--;
                //数值从1到0说明是从前台切到后台
                LoggerUtil.d("app","onActivityStopped"+activityStartCount);
                if (activityStartCount == 0) {
                    LoggerUtil.d("app","从前台切到后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}
