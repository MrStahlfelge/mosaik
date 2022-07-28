package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.TextField;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.TruncationType;

import java.lang.reflect.Type;

public class ButtonSerializer implements JsonSerializer<Button>, JsonDeserializer<Button> {

    public static final String KEY_TEXT = "text";
    public static final String KEY_MAX_LINES = "maxLines";
    public static final String KEY_TRUNCATION_TYPE = "truncationType";
    public static final String KEY_TEXT_ALIGNMENT = "textAlignment";
    public static final String KEY_STYLE = "style";
    public static final String KEY_ENABLED = "enabled";

    @Override
    public JsonElement serialize(Button src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getText() != null) {
            jsonObject.add(KEY_TEXT, context.serialize(src.getText()));
        }
        if (src.getMaxLines() != 0) {
            jsonObject.add(KEY_MAX_LINES, context.serialize(src.getMaxLines()));
        }
        if (src.getTruncationType() != TruncationType.END) {
            jsonObject.add(KEY_TRUNCATION_TYPE, context.serialize(src.getTruncationType()));
        }
        if (!src.isEnabled()) {
            jsonObject.add(KEY_ENABLED, context.serialize(src.isEnabled()));
        }
        if (src.getTextAlignment() != HAlignment.CENTER) {
            jsonObject.add(KEY_TEXT_ALIGNMENT, context.serialize(src.getTextAlignment()));
        }
        if (src.getStyle() != Button.ButtonStyle.PRIMARY) {
            jsonObject.add(KEY_STYLE, context.serialize(src.getStyle()));
        }

        return jsonObject;
    }

    @Override
    public Button deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Button button = new Button();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, button, context);

        if (jsonObject.has(KEY_TEXT)) {
            button.setText(jsonObject.get(KEY_TEXT).getAsString());
        }
        if (jsonObject.has(KEY_ENABLED)) {
            button.setEnabled(jsonObject.get(KEY_ENABLED).getAsBoolean());
        }
        if (jsonObject.has(KEY_MAX_LINES)) {
            button.setMaxLines(jsonObject.get(KEY_MAX_LINES).getAsInt());
        }
        if (jsonObject.has(KEY_TRUNCATION_TYPE)) {
            button.setTruncationType(context.<TruncationType>deserialize(jsonObject.get(KEY_TRUNCATION_TYPE), TruncationType.class));
        }
        if (jsonObject.has(KEY_TEXT_ALIGNMENT)) {
            button.setTextAlignment(context.<HAlignment>deserialize(jsonObject.get(KEY_TEXT_ALIGNMENT), HAlignment.class));
        }
        if (jsonObject.has(KEY_STYLE)) {
            button.setStyle(context.<Button.ButtonStyle>deserialize(jsonObject.get(KEY_STYLE), Button.ButtonStyle.class));
        }

        return button;
    }
}
