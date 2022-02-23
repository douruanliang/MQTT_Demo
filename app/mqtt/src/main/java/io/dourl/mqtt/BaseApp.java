package io.dourl.mqtt;

import android.app.Application;

import java.lang.reflect.Method;

/**
 *
 */
public class BaseApp {

    private static Application sApplication;

    private static class BaseAppHolder {

        private static BaseApp INSTANCE = new BaseApp();
    }

    public static Application getApplication() {
        return BaseAppHolder.INSTANCE.sApplication;
    }

    private BaseApp() {
        try {
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClazz.getMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object activityThread = currentActivityThreadMethod.invoke(null);
            Method getApplicationMethod = activityThread.getClass().getMethod("getApplication");
            getApplicationMethod.setAccessible(true);
            sApplication = (Application) getApplicationMethod.invoke(activityThread);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
