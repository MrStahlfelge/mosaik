package org.ergoplatform.mosaik.backenddemo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MosaikRestController {
    @GetMapping("/")
    public String getInitialApp(HttpServletRequest request) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("templates/default_tree.json");

        String defaultTree = StreamUtils.copyToString(classPathResource.getInputStream(), Charset.defaultCharset());
        return defaultTree.replace("%ADDRESS%", request.getRequestURL().toString());
    }
}
