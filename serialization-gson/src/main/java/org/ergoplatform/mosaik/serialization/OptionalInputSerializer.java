package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.OptionalInputElement;

import java.lang.reflect.Type;

public class OptionalInputSerializer<U, T extends ViewElement & OptionalInputElement<U>> implements JsonSerializer<T>, JsonDeserializer<T> {

    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_VALUE = "value";
    public static final String KEY_MANDATORY = "mandatory";
    public static final String KEY_ON_VALUE_CHANGED = "onValueChanged";
    private final Type valueClazz;
    private final Class<T> inputValueElementClass;

    public OptionalInputSerializer(Type valueType, Class<T> inputValueElementClass) {
        this.valueClazz = valueType;
        this.inputValueElementClass = inputValueElementClass;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (!src.isEnabled()) {
            jsonObject.add(KEY_ENABLED, context.serialize(src.isEnabled()));
        }
        if (!src.isMandatory()) {
            jsonObject.add(KEY_MANDATORY, context.serialize(src.isMandatory()));
        }
        if (src.getOnValueChangedAction() != null) {
            jsonObject.add(KEY_ON_VALUE_CHANGED, new JsonPrimitive(src.getOnValueChangedAction()));
        }
        if (src.getValue() != null) {
            jsonObject.add(KEY_VALUE, context.serialize(src.getValue()));
        }

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T valueElement;
        try {
            valueElement = inputValueElementClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new JsonParseException(e);
        }
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, valueElement, context);

        if (jsonObject.has(KEY_VALUE)) {
            valueElement.setValue(context.<U>deserialize(jsonObject.get(KEY_VALUE), valueClazz));
        }
        if (jsonObject.has(KEY_ENABLED)) {
            valueElement.setEnabled(jsonObject.get(KEY_ENABLED).getAsBoolean());
        }
        if (jsonObject.has(KEY_MANDATORY)) {
            valueElement.setMandatory(jsonObject.get(KEY_MANDATORY).getAsBoolean());
        }
        if (jsonObject.has(KEY_ON_VALUE_CHANGED)) {
            valueElement.setOnValueChangedAction(jsonObject.get(KEY_ON_VALUE_CHANGED).getAsString());
        }

        return valueElement;
    }
}
