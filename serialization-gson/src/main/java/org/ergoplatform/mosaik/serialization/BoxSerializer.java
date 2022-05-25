package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.layout.VAlignment;

import java.lang.reflect.Type;

public class BoxSerializer<T extends Box> implements JsonSerializer<Box>, JsonDeserializer<Box> {

    public static final String KEY_PADDING = "padding";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_H_ALIGN = "hAlign";
    public static final String KEY_V_ALIGN = "vAlign";
    private final Class<T> boxClass;

    public BoxSerializer(Class<T> boxClass) {
        this.boxClass = boxClass;
    }

    @Override
    public JsonElement serialize(Box src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();
        serializeCommon(src, context, jsonObject);

        return jsonObject;
    }

    public static void serializeCommon(Box src, JsonSerializationContext context, JsonObject jsonObject) {
        if (src.getPadding() != Padding.NONE) {
            jsonObject.add(KEY_PADDING, context.serialize(src.getPadding()));
        }
        JsonArray children = new JsonArray();
        for (ViewElement child : src.getChildren()) {
            JsonObject childJson = context.serialize(child).getAsJsonObject();
            HAlignment childHAlignment = src.getChildHAlignment(child);
            VAlignment childVAlignment = src.getChildVAlignment(child);
            if (childHAlignment != HAlignment.CENTER) {
                childJson.add(KEY_H_ALIGN, context.serialize(childHAlignment));
            }
            if (childVAlignment != VAlignment.CENTER) {
                childJson.add(KEY_V_ALIGN, context.serialize(childVAlignment));
            }
            children.add(childJson);
        }
        jsonObject.add(KEY_CHILDREN, children);
    }

    @Override
    public Box deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Box box = null;
        try {
            box = boxClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JsonParseException("Could not instantiate class " + typeOfT, e);
        }
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, box, context);
        deserializeCommon(context, box, jsonObject);

        return box;
    }

    public static void deserializeCommon(JsonDeserializationContext context, Box box, JsonObject jsonObject) {
        if (jsonObject.has(KEY_PADDING)) {
            box.setPadding(context.<Padding>deserialize(jsonObject.get(KEY_PADDING), Padding.class));
        }
        JsonArray childrenArray = jsonObject.get(KEY_CHILDREN).getAsJsonArray();
        for (JsonElement childJson : childrenArray) {
            JsonObject childJsonObject = childJson.getAsJsonObject();
            ViewElement childElement = context.deserialize(childJson, ViewElement.class);
            HAlignment hAlignment = childJsonObject.has(KEY_H_ALIGN) ? context.<HAlignment>deserialize(childJsonObject.get(KEY_H_ALIGN), HAlignment.class) : HAlignment.CENTER;
            VAlignment vAlignment = childJsonObject.has(KEY_V_ALIGN) ? context.<VAlignment>deserialize(childJsonObject.get(KEY_V_ALIGN), VAlignment.class) : VAlignment.CENTER;
            box.addChild(childElement, hAlignment, vAlignment);
        }
    }
}
