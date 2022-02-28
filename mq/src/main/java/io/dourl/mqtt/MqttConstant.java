package io.dourl.mqtt;

/**
 * Mqtt常量类
 */
public class MqttConstant {

    /* fame mqtt server info */
    //public static String URI = "ssl://" + "BuildConfig.MQTT_HOST" + ":8883";
    public static String URI = "tcp://127.0.0.1:1883";
    public static String PAHO_TEST_URI = "tcp://iot.eclipse.org:1883";

    public static int PORT = 8883;

    public static int TIMEOUT = 30;

    public static int KEEP_ALIVE = 45;
}