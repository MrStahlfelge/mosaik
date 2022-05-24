package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.ergoplatform.mosaik.model.InitialAppInfo;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AlignmentsController {
    @GetMapping("/alignments")
    public InitialAppInfo loadAlignmentsDemoApp(@RequestHeader Map<String, String> headers,
                                             HttpServletRequest request) {
        MosaikContext context = MosaikSerializer.fromContextHeadersMap(headers);

        InitialAppInfo appInfo = new InitialAppInfo();
        MosaikManifest loadBoxManifest = new MosaikManifest("AlignmentsDemo", BackendDemoApplication.APP_VERSION, 0, null, 0);
        loadBoxManifest.baseUrl = request.getRequestURL().toString();
        appInfo.setManifest(loadBoxManifest);

        Column container = new Column();

        Label headline = new Label();
        headline.setText("Alignments demo");
        headline.setStyle(LabelStyle.HEADLINE2);
        container.addChild(headline);

        for (HAlignment value : HAlignment.values()) {
            Button button = new Button();
            button.setText(value.name());

            container.addChild(button, value, 0);
        }

        appInfo.setView(container);
        return appInfo;
    }
}
