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

    @Override
    public JsonElement serialize(TokenLabel src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src, ViewElement.class).getAsJsonObject();

        if (src.getTokenName() != null) {
            jsonObject.add("tokenId", context.serialize(src.getTokenId()));
        }
        if (src.getTokenId() != null) {
            jsonObject.add("tokenName", context.serialize(src.getTokenName()));
        }
        jsonObject.add("decimals", new JsonPrimitive(src.getDecimals()));
        jsonObject.add("amount", new JsonPrimitive(src.getAmount()));

        return jsonObject;
    }

    @Override
    public TokenLabel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TokenLabel tokenLabel = new TokenLabel();
        ViewElementSerializer.deserializeCommon(json.getAsJsonObject(), tokenLabel, context);
        return tokenLabel;
    }
}
