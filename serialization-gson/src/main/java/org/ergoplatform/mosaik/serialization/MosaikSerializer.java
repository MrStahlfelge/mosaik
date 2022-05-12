package org.ergoplatform.mosaik.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.TextInputField;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

public class MosaikSerializer {
    static final String TYPE_ELEMENT_NAME = "type";

    public String toJson(ViewElement element) {
        Gson gson = getGson(false);
        return gson.toJson(element);
    }

    public String toJsonBeautified(ViewElement element) {
        Gson gson = getGson(true);
        return gson.toJson(element);
    }

    private Gson getGson(boolean prettyPrint) {
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
        gsonBuilder.registerTypeAdapter(Label.class, new LabelSerializer());
        gsonBuilder.registerTypeAdapter(LoadingIndicator.class, new LoadingIndicatorSerializer());
        gsonBuilder.registerTypeAdapter(LazyLoadBox.class, new LazyLoadBoxSerializer());
        gsonBuilder.registerTypeAdapter(Icon.class, new IconSerializer());

        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }

        return gsonBuilder.create();
    }

    public ViewElement viewElementFromJson(String json) {
        Gson gson = getGson(false);
        return gson.fromJson(json, ViewElement.class);
    }
}