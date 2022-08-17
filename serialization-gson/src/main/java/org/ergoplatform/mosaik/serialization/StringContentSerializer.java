package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.StringContentElement;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import java.lang.reflect.Type;

public class StringContentSerializer<T extends ViewElement & StringContentElement> implements JsonSerializer<T>, JsonDeserializer<T> {

    public static final String KEY_CONTENT = "content";
    public static final String KEY_ALIGNMENT = "contentAlignment";
    private final Class<T> valueElementClass;

    public StringContentSerializer(Class<T> valueElementClass) {
        this.valueElementClass = valueElementClass;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_CONTENT, context.serialize(src.getContent()));
        if (src.getContentAlignment() != HAlignment.START) {
            jsonObject.add(KEY_ALIGNMENT, context.serialize(src.getContentAlignment()));
        }

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T valueElement;
        try {
            valueElement = valueElementClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new JsonParseException(e);
        }

        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, valueElement, context);

        valueElement.setContent(jsonObject.get(KEY_CONTENT).getAsString());

        if (jsonObject.has(KEY_ALIGNMENT)) {
            valueElement.setContentAlignment(context.<HAlignment>deserialize(jsonObject.get(KEY_ALIGNMENT), HAlignment.class));
        }

        return valueElement;
    }
}
