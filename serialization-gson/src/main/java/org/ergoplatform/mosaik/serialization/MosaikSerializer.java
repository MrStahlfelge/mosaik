package org.ergoplatform.mosaik.serialization;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.ergoplatform.mosaik.model.FetchActionResponse;
import org.ergoplatform.mosaik.model.MosaikApp;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.NotificationCheckResponse;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.Image;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.MarkDown;
import org.ergoplatform.mosaik.model.ui.QrCode;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.CheckboxLabel;
import org.ergoplatform.mosaik.model.ui.input.DecimalInputField;
import org.ergoplatform.mosaik.model.ui.input.DropDownList;
import org.ergoplatform.mosaik.model.ui.input.ErgAddressInputField;
import org.ergoplatform.mosaik.model.ui.input.ErgAmountInputField;
import org.ergoplatform.mosaik.model.ui.input.ErgoAddressChooseButton;
import org.ergoplatform.mosaik.model.ui.input.FiatOrErgAmountInputField;
import org.ergoplatform.mosaik.model.ui.input.IntegerInputField;
import org.ergoplatform.mosaik.model.ui.input.PasswordInputField;
import org.ergoplatform.mosaik.model.ui.input.TextInputField;
import org.ergoplatform.mosaik.model.ui.input.WalletChooseButton;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Card;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Grid;
import org.ergoplatform.mosaik.model.ui.layout.HorizontalRule;
import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.ErgAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.ErgoAddressLabel;
import org.ergoplatform.mosaik.model.ui.text.FiatAmountLabel;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MosaikSerializer {
    static final String TYPE_ELEMENT_NAME = "type";
    static final String HTTP_HEADER_PREFIX = "Mosaik-";

    public String toJson(ViewContent content) {
        Gson gson = getGson(false);
        return gson.toJson(content);
    }

    /**
     * @return map of key, value for MosaikContext http header fields
     */
    public Map<String, String> contextHeadersMap(MosaikContext context, String referrer) {
        HashMap<String, String> retMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : getGson(false).toJsonTree(context).getAsJsonObject().entrySet()) {
            retMap.put(HTTP_HEADER_PREFIX + stringJsonElementEntry.getKey(),
                    stringJsonElementEntry.getValue().getAsString());
        }
        // we also use some extra fields
        retMap.put("User-Agent", context.walletAppName +
                "(" + context.walletAppVersion + "/" + context.walletAppPlatform + ")");
        retMap.put("Accept-Language", context.language);
        if (referrer != null) {
            retMap.put("Referer", referrer);
        }

        return retMap;
    }

    public Map<String, ?> getValuesMap(String json) {
        return getGson(false).fromJson(json, Map.class);
    }

    public MosaikContext fromContextHeadersMap(Map<String, String> headersMap) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> headerEntry : headersMap.entrySet()) {
            if (headerEntry.getKey().toLowerCase(Locale.ROOT).startsWith(HTTP_HEADER_PREFIX.toLowerCase())) {
                jsonObject.addProperty(headerEntry.getKey().substring(HTTP_HEADER_PREFIX.length()).toLowerCase(), headerEntry.getValue());
            }
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        // http header names are lower case, so we need a field naming strategy here
        gsonBuilder.setFieldNamingStrategy(new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return f.getName().toLowerCase();
            }
        });
        return gsonBuilder.create().fromJson(jsonObject, MosaikContext.class);
    }

    public String toJson(FetchActionResponse actionResponse) {
        return getGson(false).toJson(actionResponse);
    }

    public String toJson(MosaikApp appInfo) {
        return getGson(false).toJson(appInfo);
    }

    public String toJsonBeautified(ViewContent content) {
        Gson gson = getGson(true);
        return gson.toJson(content);
    }

    private Gson getGson(boolean prettyPrint) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ViewElement.class, new ViewElementSerializer());
        gsonBuilder.registerTypeAdapter(Box.class, new BoxSerializer<>(Box.class));
        gsonBuilder.registerTypeAdapter(Card.class, new BoxSerializer<>(Card.class));
        gsonBuilder.registerTypeAdapter(Grid.class, new GridSerializer());
        gsonBuilder.registerTypeAdapter(Row.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(Row.class, new RowDeserializer());
        gsonBuilder.registerTypeAdapter(Column.class, new LinearLayoutSerializer());
        gsonBuilder.registerTypeAdapter(Column.class, new ColumnDeserializer());
        gsonBuilder.registerTypeAdapter(TokenLabel.class, new TokenLabelSerializer());
        gsonBuilder.registerTypeAdapter(Action.class, new ActionAdapter());
        gsonBuilder.registerTypeAdapter(TextInputField.class, new TextFieldSerializer<>(String.class, TextInputField.class));
        gsonBuilder.registerTypeAdapter(PasswordInputField.class, new TextFieldSerializer<>(String.class, PasswordInputField.class));
        gsonBuilder.registerTypeAdapter(ErgAddressInputField.class, new TextFieldSerializer<>(String.class, ErgAddressInputField.class));
        gsonBuilder.registerTypeAdapter(FiatOrErgAmountInputField.class, new TextFieldSerializer<>(Long.class, FiatOrErgAmountInputField.class));
        gsonBuilder.registerTypeAdapter(IntegerInputField.class, new TextFieldSerializer<>(Long.class, IntegerInputField.class));
        gsonBuilder.registerTypeAdapter(DecimalInputField.class, new TextFieldSerializer<>(Long.class, DecimalInputField.class));
        gsonBuilder.registerTypeAdapter(ErgAmountInputField.class, new TextFieldSerializer<>(Long.class, ErgAmountInputField.class));
        gsonBuilder.registerTypeAdapter(CheckboxLabel.class,
                new OptionalInputWithLabelSerializer<>(new TypeToken<Boolean>() {
                }.getType(), CheckboxLabel.class));
        gsonBuilder.registerTypeAdapter(ErgoAddressChooseButton.class,
                new OptionalInputSerializer<>(new TypeToken<String>() {
                }.getType(), ErgoAddressChooseButton.class));
        gsonBuilder.registerTypeAdapter(WalletChooseButton.class,
                new OptionalInputSerializer<>(new TypeToken<List<String>>() {
                }.getType(), WalletChooseButton.class));
        gsonBuilder.registerTypeAdapter(Button.class, new ButtonSerializer());
        gsonBuilder.registerTypeAdapter(Label.class, new LabelSerializer<>(String.class, Label.class));
        gsonBuilder.registerTypeAdapter(ErgoAddressLabel.class, new LabelSerializer<>(String.class, ErgoAddressLabel.class));
        gsonBuilder.registerTypeAdapter(ErgAmountLabel.class, new LabelSerializer<>(Long.class, ErgAmountLabel.class));
        gsonBuilder.registerTypeAdapter(FiatAmountLabel.class, new LabelSerializer<>(Long.class, FiatAmountLabel.class));
        gsonBuilder.registerTypeAdapter(LoadingIndicator.class, new LoadingIndicatorSerializer());
        gsonBuilder.registerTypeAdapter(LazyLoadBox.class, new LazyLoadBoxSerializer());
        gsonBuilder.registerTypeAdapter(Icon.class, new IconSerializer());
        gsonBuilder.registerTypeAdapter(Image.class, new ImageSerializer());
        gsonBuilder.registerTypeAdapter(QrCode.class, new StringContentSerializer<>(QrCode.class));
        gsonBuilder.registerTypeAdapter(MarkDown.class, new StringContentSerializer<>(MarkDown.class));
        gsonBuilder.registerTypeAdapter(HorizontalRule.class, new HorizontalRuleSerializer());
        gsonBuilder.registerTypeAdapter(DropDownList.class, new DropDownListSerializer());

        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }

        return gsonBuilder.create();
    }

    public ViewContent viewContentFromJson(String json) {
        try {
            Gson gson = getGson(false);
            return gson.fromJson(json, ViewContent.class);
        } catch (Throwable t) {
            throw new DeserializationException(t);
        }
    }

    public MosaikApp firstRequestResponseFromJson(String json) {
        try {
            return getGson(false).fromJson(json, MosaikApp.class);
        } catch (Throwable t) {
            throw new DeserializationException(t);
        }
    }

    public FetchActionResponse fetchActionResponseFromJson(String json) {
        try {
            return getGson(false).fromJson(json, FetchActionResponse.class);
        } catch (Throwable t) {
            throw new DeserializationException(t);
        }
    }

    public NotificationCheckResponse notificationCheckResponseFromJson(String json) {
        try {
            return getGson(false).fromJson(json, NotificationCheckResponse.class);
        } catch (Throwable t) {
            throw new DeserializationException(t);
        }
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