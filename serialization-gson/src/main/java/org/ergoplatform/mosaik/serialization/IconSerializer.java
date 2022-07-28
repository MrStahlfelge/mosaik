package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.lang.reflect.Type;

public class IconSerializer implements JsonSerializer<Icon>, JsonDeserializer<Icon> {

    public static final String KEY_ICON_TYPE = "icon";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TINT_COLOR = "tintColor";

    @Override
    public JsonElement serialize(Icon src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_ICON_TYPE, context.serialize(src.getIconType(), IconType.class));
        jsonObject.add(KEY_SIZE, context.serialize(src.getIconSize(), Icon.Size.class));
        if (src.getTintColor() != ForegroundColor.DEFAULT) {
            jsonObject.add(KEY_TINT_COLOR, context.serialize(src.getTintColor(), ForegroundColor.class));
        }

        return jsonObject;
    }

    @Override
    public Icon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Icon icon = new Icon();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, icon, context);

        if (jsonObject.has(KEY_TINT_COLOR)) {
            icon.setTintColor(context.<ForegroundColor>deserialize(jsonObject.get(KEY_TINT_COLOR), ForegroundColor.class));
        }
        icon.setIconSize(context.<Icon.Size>deserialize(jsonObject.get(KEY_SIZE), Icon.Size.class));

        try {
            icon.setIconType(context.<IconType>deserialize(jsonObject.get(KEY_ICON_TYPE), IconType.class));
        } catch (Throwable t) {
            // unknown icon type, we swallow this error in this special case
            icon.setIconType(IconType.ERROR);
        }

        return icon;
    }
}
