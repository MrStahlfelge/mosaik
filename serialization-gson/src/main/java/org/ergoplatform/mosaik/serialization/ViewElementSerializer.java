package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.Image;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.QrCode;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.DecimalInputField;
import org.ergoplatform.mosaik.model.ui.input.DropDownList;
import org.ergoplatform.mosaik.model.ui.input.ErgAmountInputField;
import org.ergoplatform.mosaik.model.ui.input.ErgoAddressChooseButton;
import org.ergoplatform.mosaik.model.ui.input.IntegerInputField;
import org.ergoplatform.mosaik.model.ui.input.PasswordInputField;
import org.ergoplatform.mosaik.model.ui.input.TextInputField;
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Card;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HorizontalRule;
import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.ErgoAddressLabel;
import org.ergoplatform.mosaik.model.ui.text.FiatAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Type;

public class ViewElementSerializer implements JsonSerializer<ViewElement>, JsonDeserializer<ViewElement> {

    public static final String KEY_VISIBLE = "visible";
    public static final String KEY_ID = "id";
    public static final String KEY_LONG_PRESS = "onLongPress";
    public static final String KEY_CLICK = "onClick";

    @Override
    public JsonElement serialize(ViewElement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add(MosaikSerializer.TYPE_ELEMENT_NAME, new JsonPrimitive(src.getClass().getSimpleName()));
        if (!src.isVisible()) {
            jsonObject.add(KEY_VISIBLE, new JsonPrimitive(src.isVisible()));
        }
        if (src.getId() != null) {
            jsonObject.add(KEY_ID, new JsonPrimitive(src.getId()));
        }
        if (src.getOnLongPressAction() != null) {
            jsonObject.add(KEY_LONG_PRESS, new JsonPrimitive(src.getOnLongPressAction()));
        }
        if (src.getOnClickAction() != null) {
            jsonObject.add(KEY_CLICK, new JsonPrimitive(src.getOnClickAction()));
        }

        return jsonObject;
    }

    @Override
    public ViewElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String elementName = json.getAsJsonObject().get(MosaikSerializer.TYPE_ELEMENT_NAME).getAsString();

        // no reflection used here, this runs on mobile devices
        Class<?> clazz;
        switch (elementName) {
            case "TokenLabel":
                clazz = TokenLabel.class;
                break;
            case "ErgAmountLabel":
                clazz = ErgAmountLabel.class;
                break;
            case "LazyLoadBox":
                clazz = LazyLoadBox.class;
                break;
            case "Row":
                clazz = Row.class;
                break;
            case "StyleableTextLabel":
                clazz = StyleableTextLabel.class;
                break;
            case "Label":
                clazz = Label.class;
                break;
            case "ErgoAddressLabel":
                clazz = ErgoAddressLabel.class;
                break;
            case "WalletChooseButton":
                clazz = WalletChooseButton.class;
                break;
            case "Button":
                clazz = Button.class;
                break;
            case "Box":
                clazz = Box.class;
                break;
            case "Card":
                clazz = Card.class;
                break;
            case "TextInputField":
                clazz = TextInputField.class;
                break;
            case "PasswordInputField":
                clazz = PasswordInputField.class;
                break;
            case "IntegerInputField":
                clazz = IntegerInputField.class;
                break;
            case "PasswordTextField":
                clazz = PasswordInputField.class;
                break;
            case "ErgAmountInputField":
                clazz = ErgAmountInputField.class;
                break;
            case "ErgoAddressChooseButton":
                clazz = ErgoAddressChooseButton.class;
                break;
            case "Column":
                clazz = Column.class;
                break;
            case "LoadingIndicator":
                clazz = LoadingIndicator.class;
                break;
            case "DecimalInputField":
                clazz = DecimalInputField.class;
                break;
            case "QrCode":
                clazz = QrCode.class;
                break;
            case "Icon":
                clazz = Icon.class;
                break;
            case "FiatAmountLabel":
                clazz = FiatAmountLabel.class;
                break;
            case "Image":
                clazz = Image.class;
                break;
            case "HorizontalRule":
                clazz = HorizontalRule.class;
                break;
            case "DropDownList":
                clazz = DropDownList.class;
                break;
            default:
                throw new JsonParseException("View Element with name " + elementName + " not known.");
        }

        try {
            return context.deserialize(json, clazz);
        } catch (StackOverflowError e) {
            throw new IllegalArgumentException("No deserializer for element type " + elementName);
        }
    }

    public static void deserializeCommon(JsonObject json, ViewElement element, JsonDeserializationContext context) {
        if (json.has(KEY_VISIBLE)) {
            element.setVisible(json.get(KEY_VISIBLE).getAsBoolean());
        }
        if (json.has(KEY_ID)) {
            element.setId(json.get(KEY_ID).getAsString());
        }
        if (json.has(KEY_CLICK)) {
            element.setOnClickAction(json.get(KEY_CLICK).getAsString());
        }
        if (json.has(KEY_LONG_PRESS)) {
            element.setOnLongPressAction(json.get(KEY_CLICK).getAsString());
        }
    }
}
