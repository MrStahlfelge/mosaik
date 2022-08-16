package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.ergoplatform.mosaik.model.MosaikApp;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.Icon;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.LoadingIndicator;
import org.ergoplatform.mosaik.model.ui.input.TextInputField;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class LazyBoxDemoController {
    @GetMapping("/lazybox")
    public MosaikApp loadLazyBoxDemoApp(@RequestHeader Map<String, String> headers) {
        MosaikContext context = MosaikSerializer.fromContextHeadersMap(headers);

        MosaikApp appInfo = new MosaikApp();
        MosaikManifest loadBoxManifest = new MosaikManifest("LazyLoadBoxDemo", BackendDemoApplication.APP_VERSION, MosaikContext.LIBRARY_MOSAIK_VERSION, null, 0);
        appInfo.setManifest(loadBoxManifest);

        Column container = new Column();

        Label headline = new Label();
        headline.setText("Lazy loadbox demo");
        headline.setStyle(LabelStyle.HEADLINE2);

        Label description = new Label();
        description.setText("This example demonstrates how to use a lazy loadbox to fetch content that may take more time to load without affecting user interaction.");

        Box textBoxPadding = new Box();
        textBoxPadding.setPadding(Padding.DEFAULT);
        TextInputField textField = new TextInputField();
        textField.setId("textinput"); // ID is needed in order to store the user entered values
        textField.setPlaceholder("You can enter text here without problems while the text at the bottom is loading.");
        textBoxPadding.addChild(textField);

        LazyLoadBox lazyBox = new LazyLoadBox();
        LoadingIndicator indicator = new LoadingIndicator();
        lazyBox.addChild(indicator);
        lazyBox.setRequestUrl("boxcontents");

        Icon errorView = new Icon();
        errorView.setIconType(IconType.ERROR);
        errorView.setTintColor(ForegroundColor.PRIMARY);

        lazyBox.setErrorView(errorView);

        Box loadBoxPadding = new Box();
        loadBoxPadding.setPadding(Padding.DEFAULT);
        loadBoxPadding.addChild(lazyBox);

        container.addChild(headline);
        container.addChild(description);
        container.addChild(textBoxPadding);
        container.addChild(loadBoxPadding);
        appInfo.setView(container);
        return appInfo;
    }

    @GetMapping("lazybox/boxcontents")
    public ViewContent boxContents(@RequestHeader Map<String, String> headers) throws InterruptedException {
        MosaikContext context = MosaikSerializer.fromContextHeadersMap(headers);
        Thread.sleep(5000);

        Label text = new Label();
        text.setText("This text was loaded later; you are using " + context.walletAppName);

        return new ViewContent(text);
    }
}
