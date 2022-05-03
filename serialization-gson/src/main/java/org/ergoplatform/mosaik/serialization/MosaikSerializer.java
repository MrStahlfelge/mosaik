package org.ergoplatform.mosaik.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.ViewElement;

public class MosaikSerializer {
    public static String toJson(ViewElement element) {
        Gson gson = getGson();
        return gson.toJson(element, ViewElement.class);
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ViewElement.class, new ViewElementAdapter());
        gsonBuilder.registerTypeAdapter(Action.class, new ActionAdapter());
        return gsonBuilder.create();
    }

    public static ViewElement viewElementFromJson(String json) {
        Gson gson = getGson();
        return gson.fromJson(json, ViewElement.class);
    }
}