package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.Image;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.lang.reflect.Type;

public class ImageSerializer implements JsonSerializer<Image>, JsonDeserializer<Image> {

    public static final String KEY_URL = "url";
    public static final String KEY_SIZE = "size";

    @Override
    public JsonElement serialize(Image src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_URL, context.serialize(src.getUrl()));
        jsonObject.add(KEY_SIZE, context.serialize(src.getSize(), Image.Size.class));

        return jsonObject;
    }

    @Override
    public Image deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Image image = new Image();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, image, context);

        image.setSize(context.<Image.Size>deserialize(jsonObject.get(KEY_SIZE), Image.Size.class));
        image.setUrl(jsonObject.get(KEY_URL).getAsString());

        return image;
    }
}
