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
import org.ergoplatform.mosaik.model.ui.layout.Grid;
import org.ergoplatform.mosaik.model.ui.layout.Padding;

import java.lang.reflect.Type;

public class GridSerializer implements JsonSerializer<Grid>, JsonDeserializer<Grid> {

    public static final String KEY_PADDING = "padding";
    public static final String KEY_CHILDREN = "children";
    public static final String KEY_SIZE = "size";

    @Override
    public JsonElement serialize(Grid src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getPadding() != Padding.NONE) {
            jsonObject.add(KEY_PADDING, context.serialize(src.getPadding()));
        }
        if (src.getElementSize() != Grid.ElementSize.SMALL) {
            jsonObject.add(KEY_SIZE, context.serialize(src.getElementSize()));
        }
        if (!src.getChildren().isEmpty()) {
            JsonArray children = new JsonArray();
            for (ViewElement child : src.getChildren()) {
                JsonObject childJson = context.serialize(child).getAsJsonObject();
                children.add(childJson);
            }
            jsonObject.add(KEY_CHILDREN, children);
        }
        return jsonObject;
    }

    @Override
    public Grid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Grid grid = new Grid();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, grid, context);

        if (jsonObject.has(KEY_PADDING)) {
            grid.setPadding(context.<Padding>deserialize(jsonObject.get(KEY_PADDING), Padding.class));
        }
        if (jsonObject.has(KEY_SIZE)) {
            grid.setElementSize(context.<Grid.ElementSize>deserialize(jsonObject.get(KEY_SIZE), Grid.ElementSize.class));
        }
        if (jsonObject.has(KEY_CHILDREN)) {
            JsonArray childrenArray = jsonObject.get(KEY_CHILDREN).getAsJsonArray();
            for (JsonElement childJson : childrenArray) {
                ViewElement childElement = context.deserialize(childJson, ViewElement.class);
                grid.addChild(childElement);
            }
        }

        return grid;
    }
}
