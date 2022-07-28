package org.ergoplatform.mosaik.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Map;

public class MosaikSerializer {
    static final String HTTP_HEADER_PREFIX = "mosaik-";

    public static ObjectMapper getMosaikMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Action.class, new ActionSerializer(null));
        module.addSerializer(ViewElement.class, new ViewElementSerializer(null));
        module.addSerializer(ViewContent.class, new ViewContentSerializer(null));
        mapper.registerModule(module);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static MosaikContext fromContextHeadersMap(Map<String, String> headersMap) {
        return new MosaikContext(Integer.parseInt(headersMap.getOrDefault(HTTP_HEADER_PREFIX + "mosaikversion", "0")),
                headersMap.get(HTTP_HEADER_PREFIX + "guid"),
                headersMap.get(HTTP_HEADER_PREFIX + "language"),
                headersMap.get(HTTP_HEADER_PREFIX + "walletappname"),
                headersMap.get(HTTP_HEADER_PREFIX + "walletappversion"),
                MosaikContext.Platform.valueOf(headersMap.get(HTTP_HEADER_PREFIX + "walletappplatform"))
        );
    }
}