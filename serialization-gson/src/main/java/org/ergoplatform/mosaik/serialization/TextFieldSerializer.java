package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.TextField;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Type;

public class TextFieldSerializer<U, T extends TextField<U>> implements JsonSerializer<T>, JsonDeserializer<T> {

    public static final String KEY_ERROR_MESSAGE = "error";
    public static final String KEY_VALUE = "value";
    public static final String KEY_PLACEHOLDER = "placeholder";
    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_END_ICON = "endIcon";
    public static final String KEY_ON_END_ICON_CLICKED = "onEndIconClicked";
    public static final String KEY_ON_VALUE_CHANGED = "onValueChanged";
    private final Class<U> valueClazz;
    private final Class<T> textFieldClass;

    public TextFieldSerializer(Class<U> valueClazz, Class<T> textFieldClass) {
        this.valueClazz = valueClazz;
        this.textFieldClass = textFieldClass;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getErrorMessage() != null) {
            jsonObject.add(KEY_ERROR_MESSAGE, context.serialize(src.getErrorMessage()));
        }
        if (src.getValue() != null) {
            jsonObject.add(KEY_VALUE, context.serialize(src.getValue()));
        }
        if (src.getPlaceHolder() != null) {
            jsonObject.add(KEY_PLACEHOLDER, context.serialize(src.getPlaceHolder()));
        }
        if (!src.isEnabled()) {
            jsonObject.add(KEY_ENABLED, context.serialize(src.isEnabled()));
        }
        if (src.getEndIcon() != null) {
            jsonObject.add(KEY_END_ICON, context.serialize(src.getEndIcon()));
        }
        if (src.getOnEndIconClicked() != null) {
            jsonObject.add(KEY_ON_END_ICON_CLICKED, new JsonPrimitive(src.getOnEndIconClicked()));
        }
        if (src.getOnValueChangedAction() != null) {
            jsonObject.add(KEY_ON_VALUE_CHANGED, new JsonPrimitive(src.getOnValueChangedAction()));
        }

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T textInputField;
        try {
            textInputField = textFieldClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new JsonParseException(e);
        }
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, textInputField, context);

        if (jsonObject.has(KEY_ERROR_MESSAGE)) {
            textInputField.setErrorMessage(jsonObject.get(KEY_ERROR_MESSAGE).getAsString());
        }
        if (jsonObject.has(KEY_VALUE)) {
            textInputField.setValue(context.<U>deserialize(jsonObject.get(KEY_VALUE), valueClazz));
        }
        if (jsonObject.has(KEY_PLACEHOLDER)) {
            textInputField.setPlaceHolder(jsonObject.get(KEY_PLACEHOLDER).getAsString());
        }
        if (jsonObject.has(KEY_ENABLED)) {
            textInputField.setEnabled(jsonObject.get(KEY_ENABLED).getAsBoolean());
        }
        if (jsonObject.has(KEY_END_ICON)) {
            textInputField.setEndIcon(context.<IconType>deserialize(jsonObject.get(KEY_END_ICON), IconType.class));
        }
        if (jsonObject.has(KEY_ON_END_ICON_CLICKED)) {
            textInputField.setOnEndIconClicked(jsonObject.get(KEY_ON_END_ICON_CLICKED).getAsString());
        }
        if (jsonObject.has(KEY_ON_VALUE_CHANGED)) {
            textInputField.setOnValueChangedAction(jsonObject.get(KEY_ON_VALUE_CHANGED).getAsString());
        }

        return textInputField;
    }
}
