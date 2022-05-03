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
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.QrCode;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.DecimalTextField;
import org.ergoplatform.mosaik.model.ui.input.ErgAmountTextField;
import org.ergoplatform.mosaik.model.ui.input.ErgoAddressChooseButton;
import org.ergoplatform.mosaik.model.ui.input.IntegerTextField;
import org.ergoplatform.mosaik.model.ui.input.PasswordTextField;
import org.ergoplatform.mosaik.model.ui.input.TextInputField;
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Card;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.ErgoAddressLabel;
import org.ergoplatform.mosaik.model.ui.text.FiatAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.StyleableTextLabel;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Type;
import java.util.Map;

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
            jsonObject.add(KEY_LONG_PRESS, context.serialize(src.getOnLongPressAction(), Action.class));
        }
        if (src.getOnClickAction() != null) {
            jsonObject.add(KEY_CLICK, context.serialize(src.getOnClickAction(), Action.class));
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
            case "IntegerTextField":
                clazz = IntegerTextField.class;
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
            case "PasswordTextField":
                clazz = PasswordTextField.class;
                break;
            case "ErgAmountTextField":
                clazz = ErgAmountTextField.class;
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
            case "DecimalTextField":
                clazz = DecimalTextField.class;
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
            default:
                throw new JsonParseException("View Element with name " + elementName + " not known.");
        }

        return context.deserialize(json, clazz);
    }

    public static void deserializeCommon(JsonObject json, ViewElement element, JsonDeserializationContext context) {
        if (json.has(KEY_VISIBLE)) {
            element.setVisible(json.get(KEY_VISIBLE).getAsBoolean());
        }
        if (json.has(KEY_ID)) {
            element.setId(json.get(KEY_ID).getAsString());
        }
        if (json.has(KEY_CLICK)) {
            element.setOnClickAction(context.<Action>deserialize(json.get(KEY_CLICK), Action.class));
        }
        if (json.has(KEY_LONG_PRESS)) {
            element.setOnLongPressAction(context.<Action>deserialize(json.get(KEY_CLICK), Action.class));
        }
    }
}
