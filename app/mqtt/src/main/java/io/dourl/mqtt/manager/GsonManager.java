package io.dourl.mqtt.manager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/23
 */
public class GsonManager {
    private static Gson mGson = new GsonBuilder().create();

    /**
     * @return 默认配置的Gson对象
     */
    public static Gson getGson() {
        return mGson;
    }

    public static Gson getGsonWithExclusionStrategies(ExclusionStrategy exclusionStrategy) {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(exclusionStrategy);
        return builder.create();
    }



    public static Gson getPrettyGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

}
