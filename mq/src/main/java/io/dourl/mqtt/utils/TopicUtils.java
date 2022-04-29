package io.dourl.mqtt.utils;
import io.dourl.mqtt.manager.LoginManager;


/**
 * Topic 工具
 */
public class TopicUtils {

    public static String getImTopic() {
        return "user/" + LoginManager.getInstance().getCurrentUserId();
    }

    public static String getGimTopic(String topic) {
        return "g/" + topic;
    }

    public static String getNewsTopic() {
        return "news";
    }

    public static String getDefaultClanTopic() {
        return "Official";
    }
}
