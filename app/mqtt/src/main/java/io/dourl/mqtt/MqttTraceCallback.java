package io.dourl.mqtt;

import android.util.Log;

import org.eclipse.paho.android.service.MqttTraceHandler;

/**
 * tracecallback
 */
public class MqttTraceCallback implements MqttTraceHandler {

    public static final String TAG = "mqtt";

    @Override
    public void traceDebug(String source, String message) {
        Log.d(TAG, String.format("sourse :%s, message: %s", source, message));
    }

    @Override
    public void traceError(String source, String message) {
        Log.e(TAG, String.format("sourse :%s, message: %s", source, message));
    }

    @Override
    public void traceException(String source, String message, Exception e) {
        Log.e(TAG, String.format("sourse :%s, message: %s", e, source, message));
    }
}
