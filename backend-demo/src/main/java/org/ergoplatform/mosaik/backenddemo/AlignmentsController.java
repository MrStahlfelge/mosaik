package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.ergoplatform.mosaik.model.MosaikApp;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.Button;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class AlignmentsController {
    @GetMapping("/alignments")
    public MosaikApp loadAlignmentsDemoApp(@RequestHeader Map<String, String> headers) {
        MosaikContext context = MosaikSerializer.fromContextHeadersMap(headers);

        MosaikApp appInfo = new MosaikApp();
        MosaikManifest loadBoxManifest = new MosaikManifest("AlignmentsDemo", BackendDemoApplication.APP_VERSION, MosaikContext.LIBRARY_MOSAIK_VERSION, null, 0);
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
