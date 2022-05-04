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

    public String toJson(ViewElement element) {
        Gson gson = getGson();
        return gson.toJson(element);
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ViewElement.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Box.class, new BoxSerializer());
        gsonBuilder.registerTypeAdapter(Row.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(Row.class, new RowDeserializer());
        gsonBuilder.registerTypeAdapter(Column.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(Column.class, new ColumnDeserializer());
        gsonBuilder.registerTypeAdapter(TokenLabel.class, new TokenLabelSerializer());
        gsonBuilder.registerTypeAdapter(Action.class, new ActionAdapter());
        gsonBuilder.registerTypeAdapter(TextInputField.class, new TextFieldSerializer<>(String.class, TextInputField.class));
        gsonBuilder.registerTypeAdapter(Button.class, new ButtonSerializer());
        return gsonBuilder.create();
    }

    public ViewElement viewElementFromJson(String json) {
        Gson gson = getGson();
        return gson.fromJson(json, ViewElement.class);
    }
}