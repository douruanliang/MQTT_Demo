package io.dourl.mqtt.job.core;

import io.dourl.mqtt.manager.MqttManager;


class MqttDisconnectJob implements Runnable {

    @Override
    public void run() {
        MqttManager.getInstance().disconnect();
    }
}
