package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HorizontalRule;
import org.ergoplatform.mosaik.model.ui.layout.Padding;

import java.lang.reflect.Type;

public class HorizontalRuleSerializer implements JsonSerializer<HorizontalRule>, JsonDeserializer<HorizontalRule> {

    public static final String KEY_VERTICAL_PADDING = "vPadding";

    @Override
    public JsonElement serialize(HorizontalRule src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getvPadding() != Padding.DEFAULT) {
            jsonObject.add(KEY_VERTICAL_PADDING, context.serialize(src.getvPadding(), Padding.class));
        }

        return jsonObject;
    }

    @Override
    public HorizontalRule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        HorizontalRule element = new HorizontalRule();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, element, context);

        if (jsonObject.has(KEY_VERTICAL_PADDING)) {
            element.setvPadding(context.<Padding>deserialize(jsonObject.get(KEY_VERTICAL_PADDING), Padding.class));
        }

        return element;
    }
}
