package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import java.lang.reflect.Type;

public class ColumnDeserializer implements JsonDeserializer<Column> {
    @Override
    public Column deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Column column = new Column();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, column, context);
        LinearLayoutSerializer.deserializeCommon(jsonObject, column, column.defaultChildAlignment(), HAlignment.class, context);

        return column;
    }
}
