package io.dourl.mqtt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.dourl.http.HttpApiBase;
import io.dourl.mqtt.base.MqttBaseApp;
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
        initConstant();
        MqttBaseApp.init(this);
        HttpApiBase.init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            private int activityStartCount = 0;
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                activityStartCount++;
                //LoggerUtils.d("app","onActivityStarted"+activityStartCount);
                if (activityStartCount == 1) {
                   // LoggerUtils.d("app","从后台切到前台");
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
                //LoggerUtils.d("app","onActivityStopped"+activityStartCount);
                if (activityStartCount == 0) {
                   // LoggerUtils.d("app","从前台切到后台");
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

    private void initConstant() {
        initSysConstant();
    }

    /**
     * 初始化 系统常量
     */
    private void initSysConstant() {
        Constants.PACKAGE_NAME = DeviceInfoUtils.getPackageName(getApplication());
        Constants.FILE_PROVIDER = Constants.PACKAGE_NAME + Constants.PROVIDER;
        Constants.VERSION_CODE = DeviceInfoUtils.getVersionCode(getApplication());
        Constants.VERSION_NAME = DeviceInfoUtils.getVersionName(getApplication());
        Constants.SCREENDENSITY = DeviceInfoUtils.getScreenDensity(getApplication());
        Constants.SCREENWIDTH = DeviceInfoUtils.getScreenWidth(getApplication());
        Constants.SCREENHEIGHT = DeviceInfoUtils.getScreenHeight(getApplication());
        Constants.SCREENDENSITYDPI = DeviceInfoUtils.getScreenDensityDpi(getApplication());
        Constants.SCREENSCALEDDENSIT = DeviceInfoUtils.getScreenScaledDensity(getApplication());
        Constants.NETWORKCOUNTRYISO = DeviceInfoUtils.getNetworkCountryIso(getApplication());
        Constants.SIMCOUNTRYISO = DeviceInfoUtils.getSimCountryIso(getApplication());
        Constants.NETWORKOPERATOR = DeviceInfoUtils.getNetworkOperator(getApplication());
        Constants.SIMOPERATOR = DeviceInfoUtils.getSimOperator(getApplication());
        Constants.STATUSBAR_HEIGHT = DeviceInfoUtils.getStatusBarHeight(getApplication());
    }

    private Context getApplication() {
        return this;
    }
}
