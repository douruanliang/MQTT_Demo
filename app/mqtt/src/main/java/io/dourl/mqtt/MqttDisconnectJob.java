package io.dourl.mqtt;

/**
 * Created by zhangpeng on 16/9/20.
 */
class MqttDisconnectJob implements Runnable {

    @Override
    public void run() {
        MqttManager.getInstance().disconnect();
    }
}
