package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.layout.VAlignment;

import java.lang.reflect.Type;

public class RowDeserializer implements JsonDeserializer<Row> {
    @Override
    public Row deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Row row = new Row();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, row, context);
        LinearLayoutSerializer.deserializeCommon(jsonObject, row, row.defaultChildAlignment(), VAlignment.class, context);

        return row;
    }
}
