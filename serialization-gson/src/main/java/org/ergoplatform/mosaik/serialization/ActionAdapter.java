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
import org.ergoplatform.mosaik.model.actions.BrowserAction;
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction;
import org.ergoplatform.mosaik.model.actions.CopyClipboardAction;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.model.actions.ErgoAuthAction;
import org.ergoplatform.mosaik.model.actions.ErgoPayAction;
import org.ergoplatform.mosaik.model.actions.NavigateAction;
import org.ergoplatform.mosaik.model.actions.QrCodeAction;
import org.ergoplatform.mosaik.model.actions.BackendRequestAction;
import org.ergoplatform.mosaik.model.actions.ReloadAction;
import org.ergoplatform.mosaik.model.actions.TokenInformationAction;

import java.lang.reflect.Type;
import java.util.Map;

public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {

    public static final String TYPE_ELEMENT_NAME = "type";

    @Override
    public JsonElement serialize(Action src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonObject superObject = context.serialize(src).getAsJsonObject().getAsJsonObject();
        jsonObject.add(TYPE_ELEMENT_NAME, new JsonPrimitive(src.getClass().getSimpleName()));

        for (Map.Entry<String, JsonElement> entry : superObject.entrySet()) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }

        return jsonObject;
    }

    @Override
    public Action deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String actionName = json.getAsJsonObject().get(TYPE_ELEMENT_NAME).getAsString();

        // no reflection used here, this runs on mobile devices
        Class<?> clazz;
        switch (actionName) {
            case "ErgoPayAction":
                clazz = ErgoPayAction.class;
                break;
            case "DialogAction":
                clazz = DialogAction.class;
                break;
            case "TokenInformationAction":
                clazz = TokenInformationAction.class;
                break;
            case "ChangeSiteAction":
                clazz = ChangeSiteAction.class;
                break;
            case "CopyClipboardAction":
                clazz = CopyClipboardAction.class;
                break;
            case "BackendRequestAction":
                clazz = BackendRequestAction.class;
                break;
            case "BrowserAction":
                clazz = BrowserAction.class;
                break;
            case "QrCodeAction":
                clazz = QrCodeAction.class;
                break;
            case "NavigateAction":
                clazz = NavigateAction.class;
                break;
            case "ErgoAuthAction":
                clazz = ErgoAuthAction.class;
                break;
            case "ReloadAction":
                clazz = ReloadAction.class;
                break;
            default:
                throw new JsonParseException("Action with name " + actionName + " not known.");
        }

        return context.deserialize(json, clazz);
    }
}
