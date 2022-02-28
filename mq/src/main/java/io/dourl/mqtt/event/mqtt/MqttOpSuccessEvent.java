package io.dourl.mqtt.event.mqtt;


import io.dourl.mqtt.core.ActionListener;

/**
 * Mqtt操作成功事件
 * 包括连接，订阅，取消订阅，发布
 * Created by zhangpeng on 15/12/18.
 */
public class MqttOpSuccessEvent {

    private ActionListener.Action action;

    public MqttOpSuccessEvent(ActionListener.Action action) {
        this.action = action;
    }

    public ActionListener.Action getAction() {
        return action;
    }
}
