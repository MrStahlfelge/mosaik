package org.ergoplatform.mosaik.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;

import java.lang.reflect.Type;

public class TokenLabelSerializer implements JsonSerializer<TokenLabel>, JsonDeserializer<TokenLabel> {

    public static final String KEY_TOKEN_ID = "tokenId";
    public static final String KEY_TOKEN_NAME = "tokenName";
    public static final String KEY_DECIMALS = "decimals";
    public static final String KEY_AMOUNT = "amount";

    @Override
    public JsonElement serialize(TokenLabel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getTokenName() != null) {
            jsonObject.add(KEY_TOKEN_ID, context.serialize(src.getTokenId()));
        }
        if (src.getTokenId() != null) {
            jsonObject.add(KEY_TOKEN_NAME, context.serialize(src.getTokenName()));
        }
        jsonObject.add(KEY_DECIMALS, new JsonPrimitive(src.getDecimals()));
        jsonObject.add(KEY_AMOUNT, new JsonPrimitive(src.getAmount()));

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

        return tokenLabel;
    }
}
