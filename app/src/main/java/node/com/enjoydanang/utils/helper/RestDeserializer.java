package node.com.enjoydanang.utils.helper;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Author: Tavv
 * Created on 12/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class RestDeserializer <T> implements JsonDeserializer<T> {
    private Class<T> mClass;
    private String mKey;

    public RestDeserializer(Class<T> targetClass, String key) {
        mClass = targetClass;
        mKey = key;
    }

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        JsonElement content = je.getAsJsonObject().get(mKey);
        return new Gson().fromJson(content, mClass);

    }
}
