package org.ergoplatform.mosaik.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.ergoplatform.mosaik.model.actions.Action;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ActionSerializer extends StdSerializer<Action> {
    protected ActionSerializer(Class<Action> t) {
        super(t);
    }

    @Override
    public void serialize(Action value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("type", value.getClass().getSimpleName());

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(value.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    Object property = pd.getReadMethod().invoke(value);
                    if (property != null)
                        provider.defaultSerializeField(pd.getName(), property, jgen);
                }

            }
        } catch (IllegalArgumentException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }

        jgen.writeEndObject();
    }
}
