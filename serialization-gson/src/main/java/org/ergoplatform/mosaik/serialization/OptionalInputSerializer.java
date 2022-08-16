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
import org.ergoplatform.mosaik.model.ui.input.StyleableInputButton;

import java.lang.reflect.Type;

public class OptionalInputSerializer<U, T extends ViewElement & OptionalInputElement<U>> implements JsonSerializer<T>, JsonDeserializer<T> {

    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_VALUE = "value";
    public static final String KEY_MANDATORY = "mandatory";
    public static final String KEY_ON_VALUE_CHANGED = "onValueChanged";
    public static final String KEY_STYLE = "style";
    private final Type valueClazz;
    private final Class<T> inputValueElementClass;

    public OptionalInputSerializer(Type valueType, Class<T> inputValueElementClass) {
        this.valueClazz = valueType;
        this.inputValueElementClass = inputValueElementClass;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        serializeOptionalInputFields(src, context, jsonObject);

        return jsonObject;
    }

    protected void serializeOptionalInputFields(T src, JsonSerializationContext context, JsonObject jsonObject) {
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
        if (src instanceof StyleableInputButton && ((StyleableInputButton) src).getStyle() != StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY) {
            jsonObject.add(KEY_STYLE, context.serialize(((StyleableInputButton) src).getStyle()));
        }
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

        deserializeOptionalInputFields(context, valueElement, jsonObject);

        return valueElement;
    }

    protected void deserializeOptionalInputFields(JsonDeserializationContext context, T valueElement, JsonObject jsonObject) {
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
        if (valueElement instanceof StyleableInputButton && jsonObject.has(KEY_STYLE)) {
            ((StyleableInputButton) valueElement).setStyle(context.<StyleableInputButton.InputButtonStyle>deserialize(
                    jsonObject.get(KEY_STYLE), StyleableInputButton.InputButtonStyle.class));
        }
    }
}
