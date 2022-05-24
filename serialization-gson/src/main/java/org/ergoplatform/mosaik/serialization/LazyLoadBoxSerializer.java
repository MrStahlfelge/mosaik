package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.lang.reflect.Type;

public class LazyLoadBoxSerializer implements JsonSerializer<LazyLoadBox>, JsonDeserializer<LazyLoadBox> {

    public static final String KEY_URL = "url";
    public static final String KEY_ERROR_VIEW = "errorView";

    @Override
    public JsonElement serialize(LazyLoadBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();
        BoxSerializer.serializeCommon(src, context, jsonObject);

        jsonObject.add(KEY_URL, context.serialize(src.getRequestUrl()));
        jsonObject.add(KEY_ERROR_VIEW, context.serialize(src.getErrorView()));

        return jsonObject;
    }

    @Override
    public LazyLoadBox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LazyLoadBox box = new LazyLoadBox();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, box, context);
        BoxSerializer.deserializeCommon(context, box, jsonObject);

        box.setRequestUrl(jsonObject.get(KEY_URL).getAsString());
        box.setErrorView(context.<ViewContent>deserialize(jsonObject.getAsJsonObject(KEY_ERROR_VIEW), ViewContent.class));

        return box;
    }
}
