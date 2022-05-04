package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.Padding;

import java.lang.reflect.Type;

public class LazyLoadBoxSerializer implements JsonSerializer<LazyLoadBox>, JsonDeserializer<LazyLoadBox> {

    public static final String KEY_URL = "url";

    @Override
    public JsonElement serialize(LazyLoadBox src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();
        BoxSerializer.serializeCommon(src, context, jsonObject);

        jsonObject.add(KEY_URL, context.serialize(src.getRequestUrl()));

        return jsonObject;
    }

    @Override
    public LazyLoadBox deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LazyLoadBox box = new LazyLoadBox();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, box, context);
        BoxSerializer.deserializeCommon(context, box, jsonObject);

        box.setRequestUrl(jsonObject.get(KEY_URL).getAsString());

        return box;
    }
}
