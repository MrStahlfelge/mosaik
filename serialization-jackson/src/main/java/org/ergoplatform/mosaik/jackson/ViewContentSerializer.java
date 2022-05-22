package org.ergoplatform.mosaik.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.ergoplatform.mosaik.model.InitialAppInfo;
import org.ergoplatform.mosaik.model.ViewContent;

import java.io.IOException;

public class ViewContentSerializer extends StdSerializer<ViewContent> {

    protected ViewContentSerializer(Class<ViewContent> t) {
        super(t);
    }

    @Override
    public void serialize(ViewContent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value instanceof InitialAppInfo)
            provider.defaultSerializeField("manifest", ((InitialAppInfo) value).getManifest(), gen);
        if (!value.getActions().isEmpty())
            provider.defaultSerializeField("actions", value.getActions(), gen);
        provider.defaultSerializeField("view", value.getView(), gen);
        gen.writeEndObject();
    }
}
