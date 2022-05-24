package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.ergoplatform.mosaik.model.InitialAppInfo;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LazyBoxDemoController {
    @GetMapping("/lazybox")
    public InitialAppInfo loadLazyBoxDemoApp(@RequestHeader Map<String, String> headers,
                                             HttpServletRequest request) {
        MosaikContext context = MosaikSerializer.fromContextHeadersMap(headers);

        InitialAppInfo appInfo = new InitialAppInfo();
        MosaikManifest loadBoxManifest = new MosaikManifest("LazyLoadBoxDemo", BackendDemoApplication.APP_VERSION, 0, null, 0);
        loadBoxManifest.baseUrl = request.getRequestURL().toString();
        appInfo.setManifest(loadBoxManifest);

        Column container = new Column();

        Label headline = new Label();
        headline.setText("Lazy loadbox demo");
        headline.setStyle(LabelStyle.HEADLINE2);

        Label description = new Label();
        description.setText("This example demonstrates how to use a lazy loadbox to fetch content that may take more time to load without affecting user interaction.");

        LazyLoadBox lazyBox = new LazyLoadBox();
        LoadingIndicator indicator = new LoadingIndicator();
        lazyBox.addChild(indicator);
        lazyBox.setRequestUrl("boxcontents");

        Icon errorView = new Icon();
        errorView.setIconType(IconType.ERROR);
        errorView.setTintColor(ForegroundColor.PRIMARY);

        lazyBox.setErrorView(new ViewContent(errorView));

        Box padding = new Box();
        padding.setPadding(Padding.DEFAULT);
        padding.addChild(lazyBox);

        container.addChild(headline);
        container.addChild(description);
        container.addChild(padding);
        appInfo.setView(container);
        return appInfo;
    }

    @GetMapping("lazybox/boxcontents")
    public ViewContent boxContents() throws InterruptedException {
        Thread.sleep(2000);

        Label text = new Label();
        text.setText("This text was loaded later");

        return new ViewContent(text);
    }
}
