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

public class ViewElementAdapter implements JsonSerializer<ViewElement>, JsonDeserializer<ViewElement> {

    public static final String TYPE_ELEMENT_NAME = "type";

    @Override
    public JsonElement serialize(ViewElement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonObject superObject = context.serialize(src).getAsJsonObject().getAsJsonObject();
        jsonObject.add(TYPE_ELEMENT_NAME, new JsonPrimitive(src.getClass().getSimpleName()));

        for (Map.Entry<String, JsonElement> entry : superObject.entrySet()) {
            boolean addEntry = true;

            // include isVisible field only when not visible
            if (entry.getKey().equals("isVisible") && src.isVisible())
                addEntry = false;

            if (addEntry) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }
        }

        return jsonObject;
    }

    @Override
    public ViewElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String elementName = json.getAsJsonObject().get(TYPE_ELEMENT_NAME).getAsString();

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
}
