package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.QrCode;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.lang.reflect.Type;

public class QrCodeSerializer implements JsonSerializer<QrCode>, JsonDeserializer<QrCode> {

    public static final String KEY_CONTENT = "content";

    @Override
    public JsonElement serialize(QrCode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_CONTENT, context.serialize(src.getContent()));

        return jsonObject;
    }

    @Override
    public QrCode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        QrCode qrCode = new QrCode();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, qrCode, context);

        qrCode.setContent(jsonObject.get(KEY_CONTENT).getAsString());

        return qrCode;
    }
}
