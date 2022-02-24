package io.dourl.mqtt.job;

import com.birbit.android.jobqueue.log.CustomLogger;

import io.dourl.mqtt.base.log.LoggerUtil;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/24
 */
public class MsgJobLogger implements CustomLogger {

    private static final String TAG = "MsgJob";

    @Override
    public boolean isDebugEnabled() {
        return LoggerUtil.debugFlag;
    }

    @Override
    public void d(String text, Object... args) {
        LoggerUtil.d(TAG, String.format(text, args));
    }

    @Override
    public void e(Throwable t, String text, Object... args) {
        LoggerUtil.e(TAG, String.format(text, args), t);
    }

    @Override
    public void e(String text, Object... args) {
        LoggerUtil.e(TAG, String.format(text, args));
    }

    @Override
    public void v(String text, Object... args) {

    }
}
