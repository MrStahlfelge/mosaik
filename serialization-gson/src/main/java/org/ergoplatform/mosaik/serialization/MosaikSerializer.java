package org.ergoplatform.mosaik.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ergoplatform.mosaik.model.actions.Action;
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
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

public class MosaikSerializer {
    static final String TYPE_ELEMENT_NAME = "type";

    public static String toJson(ViewElement element) {
        Gson gson = getGson();
        return gson.toJson(element);
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ViewElement.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(DecimalTextField.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(ErgAmountTextField.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(ErgoAddressChooseButton.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(IntegerTextField.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(PasswordTextField.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(TextInputField.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(WalletChooseButton.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Box.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Card.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Row.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(LazyLoadBox.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Button.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(ErgAmountLabel.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(ErgoAddressLabel.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(FiatAmountLabel.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Label.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(LoadingIndicator.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(QrCode.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Column.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(TokenLabel.class, new TokenLabelSerializer());
        gsonBuilder.registerTypeAdapter(Action.class, new ActionAdapter());
        return gsonBuilder.create();
    }

    public static ViewElement viewElementFromJson(String json) {
        Gson gson = getGson();
        return gson.fromJson(json, ViewElement.class);
    }
}