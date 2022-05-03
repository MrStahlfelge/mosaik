package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.LinearLayout;
import org.ergoplatform.mosaik.model.ui.layout.Padding;

import java.lang.reflect.Type;

public class LinearLayoutSerializer implements JsonSerializer<LinearLayout<?>> {

    @Override
    public JsonElement serialize(LinearLayout<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getPadding() != Padding.NONE) {
            jsonObject.add("padding", context.serialize(src.getPadding()));
        }
        JsonArray children = new JsonArray();
        for (ViewElement child : src.getChildren()) {
            JsonObject childJson = context.serialize(child).getAsJsonObject();
            int childWeight = src.getChildWeight(child);
            if (childWeight != 1) {
                childJson.add("weight", new JsonPrimitive(childWeight));
            }
            Object childAlignment = src.getChildAlignment(child);
            if (childAlignment != src.defaultChildAlignment()) {
                childJson.add("alignment", context.serialize(childAlignment));
            }
            children.add(childJson);
        }
        jsonObject.add("children", children);

        return jsonObject;
    }
}
