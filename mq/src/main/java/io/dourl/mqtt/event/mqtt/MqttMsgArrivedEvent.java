package io.dourl.mqtt.event.mqtt;

/**
 * Mqtt消息到达事件
 */
public class MqttMsgArrivedEvent {
    public String topic;
    public String mqttMessage;

    public MqttMsgArrivedEvent(String topic, String mqttMessage) {
        this.topic = topic;
        this.mqttMessage = mqttMessage;
    }
}
