package io.dourl.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.Callable;

class MqttDestroyJob implements Callable<Boolean> {

    @Override
    public Boolean call() {
        try {
            MqttManager.getInstance().unSubAll();
            IMqttToken token = MqttManager.getInstance().disconnect();
            if (token != null) {
                token.waitForCompletion();
            }
            MqttManager.getInstance().clear();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }
}
