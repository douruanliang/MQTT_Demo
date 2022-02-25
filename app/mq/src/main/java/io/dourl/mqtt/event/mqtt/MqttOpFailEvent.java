package io.dourl.mqtt.event.mqtt;


import io.dourl.mqtt.ActionListener;

/**
 * Mqtt操作成功事件
 * 包括连接，订阅，取消订阅，发布
 */
public class MqttOpFailEvent {

    private ActionListener.Action action;

    public MqttOpFailEvent(ActionListener.Action action) {
        this.action = action;
    }

    public ActionListener.Action getAction() {
        return action;
    }
}
