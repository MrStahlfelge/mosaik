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
import org.ergoplatform.mosaik.model.ui.input.DropDownList;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DropDownListSerializer implements JsonSerializer<DropDownList>, JsonDeserializer<DropDownList> {

    public static final String KEY_VALUE = "value";
    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_ON_VALUE_CHANGED = "onValueChanged";
    public static final String KEY_MANDATORY = "mandatory";
    public static final String KEY_ENTRIES = "entries";
    public static final String KEY_PLACEHOLDER = "placeholder";

    @Override
    public JsonElement serialize(DropDownList src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getValue() != null) {
            jsonObject.add(KEY_VALUE, context.serialize(src.getValue()));
        }
        if (!src.isEnabled()) {
            jsonObject.add(KEY_ENABLED, context.serialize(src.isEnabled()));
        }
        if (src.getOnValueChangedAction() != null) {
            jsonObject.add(KEY_ON_VALUE_CHANGED, new JsonPrimitive(src.getOnValueChangedAction()));
        }
        if (!src.isMandatory()) {
            jsonObject.add(KEY_MANDATORY, context.serialize(src.isMandatory()));
        }
        if (src.getPlaceholder() != null) {
            jsonObject.add(KEY_PLACEHOLDER, context.serialize(src.getPlaceholder()));
        }
        jsonObject.add(KEY_ENTRIES, context.serialize(src.getEntries()));

        return jsonObject;
    }

    @Override
    public DropDownList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DropDownList dropDownList = new DropDownList();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, dropDownList, context);

        if (jsonObject.has(KEY_VALUE)) {
            dropDownList.setValue(jsonObject.get(KEY_VALUE).getAsString());
        }
        if (jsonObject.has(KEY_ENABLED)) {
            dropDownList.setEnabled(jsonObject.get(KEY_ENABLED).getAsBoolean());
        }
        if (jsonObject.has(KEY_ON_VALUE_CHANGED)) {
            dropDownList.setOnValueChangedAction(jsonObject.get(KEY_ON_VALUE_CHANGED).getAsString());
        }
        if (jsonObject.has(KEY_MANDATORY)) {
            dropDownList.setMandatory(jsonObject.get(KEY_MANDATORY).getAsBoolean());
        }
        if (jsonObject.has(KEY_PLACEHOLDER)) {
            dropDownList.setPlaceholder(jsonObject.get(KEY_PLACEHOLDER).getAsString());
        }
        dropDownList.setEntries(context.<Map>deserialize(jsonObject.get(KEY_ENTRIES), HashMap.class));

        return dropDownList;
    }
}
