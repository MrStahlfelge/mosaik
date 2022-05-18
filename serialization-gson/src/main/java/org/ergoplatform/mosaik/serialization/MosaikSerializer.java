package org.ergoplatform.mosaik.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.ergoplatform.mosaik.model.FetchActionResponse;
import org.ergoplatform.mosaik.model.InitialAppInfo;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.ViewContent;
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

import java.util.HashMap;
import java.util.Map;

public class MosaikSerializer {
    static final String TYPE_ELEMENT_NAME = "type";

    public String toJson(ViewContent content) {
        Gson gson = getGson(false);
        return gson.toJson(content);
    }

    public Map<String, String> contextMap(MosaikContext context) {
        HashMap<String, String> retMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : getGson(false).toJsonTree(context).getAsJsonObject().entrySet()) {
            retMap.put(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue().getAsString());
        }
        return retMap;
    }

    public String toJsonBeautified(ViewContent content) {
        Gson gson = getGson(true);
        return gson.toJson(content);
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

    public ViewContent viewElementFromJson(String json) {
        Gson gson = getGson(false);
        return gson.fromJson(json, ViewContent.class);
    }

    public InitialAppInfo firstRequestResponseFromJson(String json) {
        return getGson(false).fromJson(json, InitialAppInfo.class);
    }

    public FetchActionResponse fetchActionResponseFromJson(String json) {
        return getGson(false).fromJson(json, FetchActionResponse.class);
    }

    public String valuesMapToJson(Map<String, Object> values) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = getGson(false);

        for (Map.Entry<String, Object> value : values.entrySet()) {
            jsonObject.add(value.getKey(), gson.toJsonTree(value.getValue()));
        }

        return gson.toJson(jsonObject);
    }
}