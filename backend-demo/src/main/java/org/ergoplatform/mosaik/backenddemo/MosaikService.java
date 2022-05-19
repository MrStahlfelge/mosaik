package org.ergoplatform.mosaik.backenddemo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class MosaikService {
    public static final int APP_VERSION = 1;

    private final String defaultTree;

    public MosaikService() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("templates/default_tree.json");
        defaultTree = StreamUtils.copyToString(classPathResource.getInputStream(), Charset.defaultCharset()).replace("%APPVERSION%", String.valueOf(APP_VERSION));
    }

    public String getDefaultTree() {
        return defaultTree;
    }
}
