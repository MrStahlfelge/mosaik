package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.ergoplatform.mosaik.model.ui.text.TruncationType;

import java.lang.reflect.Type;

public class LabelSerializer implements JsonSerializer<Label>, JsonDeserializer<Label> {

    public static final String KEY_STYLE = "style";
    public static final String KEY_TEXT = "text";
    public static final String KEY_TEXT_COLOR = "textColor";
    public static final String KEY_MAX_LINES = "maxLines";
    public static final String KEY_TRUNCATION_TYPE = "truncationType";
    public static final String KEY_TEXT_ALIGNMENT = "textAlignment";

    @Override
    public JsonElement serialize(Label src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_STYLE, context.serialize(src.getStyle()));
        jsonObject.add(KEY_TEXT, context.serialize(src.getText()));
        if (src.getTextColor() != ForegroundColor.DEFAULT) {
            jsonObject.add(KEY_TEXT_COLOR, context.serialize(src.getTextColor()));
        }
        if (src.getMaxLines() > 0) {
            jsonObject.add(KEY_MAX_LINES, context.serialize(src.getMaxLines()));
        }
        if (src.getTruncationType() != TruncationType.END) {
            jsonObject.add(KEY_TRUNCATION_TYPE, context.serialize(src.getTruncationType()));
        }
        if (src.getTextAlignment() != HAlignment.START) {
            jsonObject.add(KEY_TEXT_ALIGNMENT, context.serialize(src.getTextAlignment()));
        }

        return jsonObject;
    }

    @Override
    public Label deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Label label = new Label();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, label, context);
        if (jsonObject.has(KEY_STYLE)) {
            label.setStyle(context.<LabelStyle>deserialize(jsonObject.get(KEY_STYLE), LabelStyle.class));
        }
        if (jsonObject.has(KEY_TEXT)) {
            label.setText(jsonObject.get(KEY_TEXT).getAsString());
        }
        if (jsonObject.has(KEY_TEXT_COLOR)) {
            label.setTextColor(context.<ForegroundColor>deserialize(jsonObject.get(KEY_TEXT_COLOR), ForegroundColor.class));
        }
        if (jsonObject.has(KEY_MAX_LINES)) {
            label.setMaxLines(jsonObject.get(KEY_MAX_LINES).getAsInt());
        }
        if (jsonObject.has(KEY_TRUNCATION_TYPE)) {
            label.setTruncationType(context.<TruncationType>deserialize(jsonObject.get(KEY_TRUNCATION_TYPE), TruncationType.class));
        }
        if (jsonObject.has(KEY_TEXT_ALIGNMENT)) {
            label.setTextAlignment(context.<HAlignment>deserialize(jsonObject.get(KEY_TEXT_ALIGNMENT), HAlignment.class));
        }

        return label;
    }
}
