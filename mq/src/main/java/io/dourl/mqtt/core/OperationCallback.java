package io.dourl.mqtt.core;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Mqtt基本操作回调
 */
public interface OperationCallback {

    void onSuccess(IMqttToken iMqttToken);

    void onFail(IMqttToken iMqttToken, Throwable throwable);

}
