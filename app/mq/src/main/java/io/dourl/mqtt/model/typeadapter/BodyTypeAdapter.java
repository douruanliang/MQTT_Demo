package io.dourl.mqtt.model.typeadapter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;

import io.dourl.mqtt.model.message.chat.BaseMsgBody;

public class BodyTypeAdapter implements JsonDeserializer<BaseMsgBody> ,JsonSerializer<BaseMsgBody>{

    @Override
    public BaseMsgBody deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.toString();
        return BaseMsgBody.buildBody(str);
    }

    @Override
    public JsonElement serialize(BaseMsgBody src, Type typeOfSrc, JsonSerializationContext context) {
        return BaseMsgBody.toJson(src);
    }


    static class Test {
        @JsonAdapter(BodyTypeAdapter.class)
        BaseMsgBody body;
    }

    public static void main(String[] args) {
        String json = "{\"body\":{\"type\":1,\"content\" : [{\"t\":\"txt\",\"c\":\"en\"}]}}";
        Test test = new Gson().fromJson(json, Test.class);
        System.out.println(test.body.toString());
    }
}
