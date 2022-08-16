package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import org.ergoplatform.mosaik.model.ui.input.OptionalInputElement;
import org.ergoplatform.mosaik.model.ui.text.Label;

import java.lang.reflect.Type;

public class OptionalInputWithLabelSerializer<U, T extends Label & OptionalInputElement<U>> extends LabelSerializer<String, T> {

    private final OptionalInputSerializer<U, T> optionalInputSerializer;

    public OptionalInputWithLabelSerializer(Type valueType, Class<T> inputValueElementClass) {
        super(String.class, inputValueElementClass);
        optionalInputSerializer = new OptionalInputSerializer<>(valueType, inputValueElementClass);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = super.serialize(src, typeOfSrc, context).getAsJsonObject();

        // now add the optional input class fields
        optionalInputSerializer.serializeOptionalInputFields(src, context, jsonObject);

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T element = super.deserialize(json, typeOfT, context);
        optionalInputSerializer.deserializeOptionalInputFields(context, element, json.getAsJsonObject());
        return element;
    }
}
