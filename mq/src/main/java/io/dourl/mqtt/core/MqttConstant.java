package io.dourl.mqtt.core;

/**
 * Mqtt常量类
 */
public class MqttConstant {

    /* fame mqtt server info */
    //public static String URI = "ssl://" + "BuildConfig.MQTT_HOST" + ":8883";
    public static String URI = "tcp://192.168.124.238:1883";
    public static String PAHO_TEST_URI = "tcp://iot.eclipse.org:1883";

    public static int PORT = 8883;

    public static int TIMEOUT = 30;

    public static int KEEP_ALIVE = 45;
}