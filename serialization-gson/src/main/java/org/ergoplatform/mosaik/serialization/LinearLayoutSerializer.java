package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.LinearLayout;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.layout.Row;

import java.lang.reflect.Type;

public class LinearLayoutSerializer implements JsonSerializer<LinearLayout<?>> {

    public static final String KEY_PADDING = "padding";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ALIGNMENT = "align";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_PACKED = "packed";
    public static final int DEFAULT_WEIGHT = 0;

    @Override
    public JsonElement serialize(LinearLayout<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getPadding() != Padding.NONE) {
            jsonObject.add(KEY_PADDING, context.serialize(src.getPadding()));
        }
        if (src instanceof Row && ((Row) src).isPacked()) {
            jsonObject.add(KEY_PACKED, context.serialize(((Row) src).isPacked()));
        }
        if (!src.getChildren().isEmpty()) {
            JsonArray children = new JsonArray();
            for (ViewElement child : src.getChildren()) {
                JsonObject childJson = context.serialize(child).getAsJsonObject();
                int childWeight = src.getChildWeight(child);
                if (childWeight != DEFAULT_WEIGHT) {
                    childJson.add(KEY_WEIGHT, new JsonPrimitive(childWeight));
                }
                Object childAlignment = src.getChildAlignment(child);
                if (childAlignment != src.defaultChildAlignment()) {
                    childJson.add(KEY_ALIGNMENT, context.serialize(childAlignment));
                }
                children.add(childJson);
            }
            jsonObject.add(KEY_CHILDREN, children);
        }

        return jsonObject;
    }

    public static <T> void deserializeCommon(JsonObject json, LinearLayout<T> layout,
                                             T defaultAlignment, Type alignmentType,
                                             JsonDeserializationContext context) {
        if (json.has(KEY_PADDING)) {
            layout.setPadding(context.<Padding>deserialize(json.get(KEY_PADDING), Padding.class));
        }
        if (json.has(KEY_PACKED) && layout instanceof Row) {
            ((Row) layout).setPacked(json.get(KEY_PACKED).getAsBoolean());
        }
        if (json.has(KEY_CHILDREN)) {
            JsonArray childrenArray = json.get(KEY_CHILDREN).getAsJsonArray();
            for (JsonElement childJson : childrenArray) {
                JsonObject childJsonObject = childJson.getAsJsonObject();
                ViewElement childElement = context.deserialize(childJson, ViewElement.class);
                int childWeight = childJsonObject.has(KEY_WEIGHT) ? childJsonObject.get(KEY_WEIGHT).getAsInt() : DEFAULT_WEIGHT;
                T alignment = childJsonObject.has(KEY_ALIGNMENT) ? context.<T>deserialize(childJsonObject.get(KEY_ALIGNMENT), alignmentType) : defaultAlignment;
                layout.addChild(childElement, alignment, childWeight);
            }
        }

    }
}
