package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Type;

public class TokenLabelSerializer implements JsonSerializer<TokenLabel>, JsonDeserializer<TokenLabel> {

    public static final String KEY_TOKEN_ID = "tokenId";
    public static final String KEY_TOKEN_NAME = "tokenName";
    public static final String KEY_DECIMALS = "decimals";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DECORATED = "decorated";
    public static final String KEY_STYLE = "style";
    public static final String KEY_TEXT_COLOR = "textColor";

    @Override
    public JsonElement serialize(TokenLabel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        jsonObject.add(KEY_TOKEN_ID, context.serialize(src.getTokenId()));
        if (src.getTokenName() != null) {
            jsonObject.add(KEY_TOKEN_NAME, context.serialize(src.getTokenName()));
        }
        if (src.getDecimals() > 0)
            jsonObject.add(KEY_DECIMALS, new JsonPrimitive(src.getDecimals()));
        if (src.getAmount() != null)
            jsonObject.add(KEY_AMOUNT, new JsonPrimitive(src.getAmount()));
        if (!src.isDecorated())
            jsonObject.add(KEY_DECORATED, new JsonPrimitive(src.isDecorated()));
        jsonObject.add(KEY_STYLE, context.serialize(src.getStyle()));
        if (src.getTextColor() != ForegroundColor.DEFAULT) {
            jsonObject.add(KEY_TEXT_COLOR, context.serialize(src.getTextColor()));
        }
        return jsonObject;
    }

    @Override
    public TokenLabel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TokenLabel tokenLabel = new TokenLabel();
        JsonObject jsonObject = json.getAsJsonObject();

        ViewElementSerializer.deserializeCommon(jsonObject, tokenLabel, context);

        if (jsonObject.has(KEY_TOKEN_ID)) {
            tokenLabel.setTokenId(jsonObject.get(KEY_TOKEN_ID).getAsString());
        }
        if (jsonObject.has(KEY_TOKEN_NAME)) {
            tokenLabel.setTokenName(jsonObject.get(KEY_TOKEN_NAME).getAsString());
        }
        if (jsonObject.has(KEY_DECIMALS)) {
            tokenLabel.setDecimals(jsonObject.get(KEY_DECIMALS).getAsInt());
        }
        if (jsonObject.has(KEY_AMOUNT)) {
            tokenLabel.setAmount(jsonObject.get(KEY_AMOUNT).getAsLong());
        }
        if (jsonObject.has(KEY_DECORATED)) {
            tokenLabel.setDecorated(jsonObject.get(KEY_DECORATED).getAsBoolean());
        }
        if (jsonObject.has(KEY_STYLE)) {
            tokenLabel.setStyle(context.<LabelStyle>deserialize(jsonObject.get(KEY_STYLE), LabelStyle.class));
        }
        if (jsonObject.has(KEY_TEXT_COLOR)) {
            tokenLabel.setTextColor(context.<ForegroundColor>deserialize(jsonObject.get(KEY_TEXT_COLOR), ForegroundColor.class));
        }

        return tokenLabel;
    }
}
