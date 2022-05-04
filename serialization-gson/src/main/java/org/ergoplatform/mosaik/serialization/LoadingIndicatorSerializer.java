package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.TruncationType;

import java.lang.reflect.Type;

public class LoadingIndicatorSerializer implements JsonSerializer<LoadingIndicator>, JsonDeserializer<LoadingIndicator> {

    public static final String KEY_SIZE = "size";

    @Override
    public JsonElement serialize(LoadingIndicator src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_SIZE, context.serialize(src.getSize()));
        return jsonObject;
    }

    @Override
    public LoadingIndicator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LoadingIndicator indicator = new LoadingIndicator();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, indicator, context);

        if (jsonObject.has(KEY_SIZE)) {
            indicator.setSize(context.<LoadingIndicator.Size>deserialize(jsonObject.get(KEY_SIZE), LoadingIndicator.Size.class));
        }

        return indicator;
    }
}
