package org.ergoplatform.mosaik.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.InputElement;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.layout.LinearLayout;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.layout.VAlignment;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.TruncationType;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ViewElementSerializer extends StdSerializer<ViewElement> {
    public static final String TYPE_ELEMENT_NAME = "type";
    public static final String KEY_PADDING = "padding";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ALIGNMENT = "align";
    public static final String KEY_H_ALIGN = "hAlign";
    public static final String KEY_V_ALIGN = "vAlign";
    public static final String KEY_CHILDREN = "children";
    public static final int DEFAULT_WEIGHT = 0;

    protected ViewElementSerializer(Class<ViewElement> t) {
        super(t);
    }

    @Override
    public void serialize(ViewElement value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        serializeObjectFields(value, jgen, provider);
        jgen.writeEndObject();
    }

    public void serializeObjectFields(ViewElement value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObjectField(TYPE_ELEMENT_NAME, value.getClass().getSimpleName());

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(value.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    Object property = pd.getReadMethod().invoke(value);
                    if (property != null) {
                        String fieldName = mapName(pd.getName(), value);

                        if (fieldName != null) {
                            if (fieldName.equals(KEY_CHILDREN) && value instanceof LinearLayout<?>) {
                                serializeLinearLayoutChildren((LinearLayout<?>) value, jgen, provider);
                            } else if (fieldName.equals(KEY_CHILDREN) && value instanceof Box) {
                                serializeBoxChildren((Box) value, jgen, provider);
                            } else {
                                provider.defaultSerializeField(fieldName, property, jgen);
                            }
                        }
                    }
                }

            }
        } catch (IllegalArgumentException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void serializeBoxChildren(Box value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        // alignment and weight written to children elements
        jgen.writeFieldName(KEY_CHILDREN);
        jgen.writeStartArray();
        for (ViewElement child : value.getChildren()) {
            jgen.writeStartObject();
            serializeObjectFields(child, jgen, provider);
            HAlignment childHAlignment = value.getChildHAlignment(child);
            VAlignment childVAlignment = value.getChildVAlignment(child);
            if (childHAlignment != HAlignment.CENTER) {
                jgen.writeObjectField(KEY_H_ALIGN, childHAlignment);
            }
            if (childVAlignment != VAlignment.CENTER) {
                jgen.writeObjectField(KEY_V_ALIGN, childVAlignment);
            }
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }

    private void serializeLinearLayoutChildren(LinearLayout<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        // alignment and weight written to children elements
        jgen.writeFieldName(KEY_CHILDREN);
        jgen.writeStartArray();
        for (ViewElement child : value.getChildren()) {
            jgen.writeStartObject();
            serializeObjectFields(child, jgen, provider);
            int childWeight = value.getChildWeight(child);
            if (childWeight != DEFAULT_WEIGHT) {
                jgen.writeObjectField(KEY_WEIGHT, childWeight);
            }
            Object childAlignment = value.getChildAlignment(child);
            if (childAlignment != value.defaultChildAlignment()) {
                provider.defaultSerializeField(KEY_ALIGNMENT, childAlignment, jgen);
            }
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }

    /**
     * maps property names to serialized name, or to null if value should be omitted from json
     */
    private String mapName(String propertyName, ViewElement value) {
        if (propertyName.equals("onClickAction")) {
            return "onClick";
        }
        if (propertyName.equals("requestUrl") && value instanceof LazyLoadBox) {
            return "url";
        }
        if (value instanceof Icon) {
            if (propertyName.equals("iconType")) {
                return "icon";
            }
            if (propertyName.equals("iconSize")) {
                return "size";
            }
            if (propertyName.equals("tintColor") && ((Icon) value).getTintColor() == ForegroundColor.DEFAULT) {
                return null;
            }
        }
        if (value instanceof Label) {
            if (propertyName.equals("maxLines") && ((Label) value).getMaxLines() == 0 ||
                    propertyName.equals("textAlignment") && ((Label) value).getTextAlignment() == HAlignment.START ||
                    propertyName.equals("textColor") && ((Label) value).getTextColor() == ForegroundColor.DEFAULT ||
                    propertyName.equals("truncationType") && ((Label) value).getTruncationType() == TruncationType.END
            )
                return null;
        }
        if (value instanceof Button) {
            if (propertyName.equals("maxLines") && ((Button) value).getMaxLines() == 0 ||
                    propertyName.equals("enabled") && ((Button) value).isEnabled() ||
                    propertyName.equals("textAlignment") && ((Button) value).getTextAlignment() == HAlignment.CENTER ||
                    propertyName.equals("style") && ((Button) value).getStyle() == Button.ButtonStyle.PRIMARY ||
                    propertyName.equals("truncationType") && ((Button) value).getTruncationType() == TruncationType.END
            )
                return null;

        }

        if (propertyName.equals("visible") && value.isVisible()) {
            return null;
        }
        if (propertyName.equals("enabled") && value instanceof InputElement && ((InputElement<?>) value).isEnabled()) {
            return null;
        }
        if (propertyName.equals(KEY_PADDING) && (value instanceof Box && ((Box) value).getPadding() == Padding.NONE || value instanceof LinearLayout<?> && ((LinearLayout<?>) value).getPadding() == Padding.NONE)) {
            return null;
        }

        return propertyName;
    }
}
