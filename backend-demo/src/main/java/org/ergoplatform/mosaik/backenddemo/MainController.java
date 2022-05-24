package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.model.InitialAppInfo;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.NavigateAction;
import org.ergoplatform.mosaik.model.ui.Image;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.layout.Row;
import org.ergoplatform.mosaik.model.ui.layout.VAlignment;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView mainPage() {
        // we always serve nobrowser error page for the main url. If the request came from a
        // Mosaik executor, it will pick up the <link rel="mosaik" ...> entry
        return new ModelAndView("nobrowser.html");
    }

    @GetMapping("/appselect")
    @ResponseBody
    public InitialAppInfo selectorApp(HttpServletRequest request) {
        MosaikManifest loadBoxManifest = new MosaikManifest("App Selector", BackendDemoApplication.APP_VERSION, 0, null, 0);
        // when writing your own app on a fixed server, you can use a constant here. Since
        // this demo might run locally on various ports and contacted through localhost or an IP
        // address, this is a safe bet to use
        String baseUrl = request.getRequestURL().toString();
        loadBoxManifest.baseUrl = baseUrl;
        String serverRequestUrl = baseUrl.substring(0, baseUrl.indexOf("appselect"));

        InitialAppInfo appInfo = new InitialAppInfo();
        appInfo.setManifest(loadBoxManifest);

        Column mainContainer = new Column();

        Label headline = new Label();
        headline.setText("Welcome to the Mosaik demo backend");
        headline.setStyle(LabelStyle.HEADLINE2);

        Label description = new Label();
        description.setText("Choose a demo to view from the buttons below");
        description.setTextAlignment(HAlignment.CENTER);

        Row row1 = new Row();

        ViewElement visitorApp = addAppButton(appInfo, "Visitor demo", "visitors/", serverRequestUrl);
        row1.addChild(visitorApp, VAlignment.CENTER, 1);

        ViewElement lazyBox = addAppButton(appInfo, "Lazy box demo", "lazybox/", serverRequestUrl);
        row1.addChild(lazyBox, VAlignment.CENTER, 1);

        Row row2 = new Row();
        ViewElement alignmentApp = addAppButton(appInfo, "Alignments demo", "alignments/", serverRequestUrl);
        row2.addChild(alignmentApp, VAlignment.CENTER, 1);
        row2.addChild(new Box(), VAlignment.CENTER, 1);

        mainContainer.addChild(headline);
        mainContainer.addChild(description);
        mainContainer.addChild(row1);
        mainContainer.addChild(row2);

        appInfo.setView(mainContainer);

        return appInfo;
    }

    private ViewElement addAppButton(ViewContent appInfo, String text, String url, String baseUrl) {
        Box container = new Box();
        container.setPadding(Padding.HALF_DEFAULT);

        Column button = new Column();
        button.setPadding(Padding.DEFAULT);

        Image buttonImage = new Image();
        buttonImage.setId("image_app" + url);
        buttonImage.setSize(Image.Size.MEDIUM);
        buttonImage.setUrl("https://picsum.photos/400");

        Label buttonLabel = new Label();
        buttonLabel.setStyle(LabelStyle.BODY1BOLD);
        buttonLabel.setTextAlignment(HAlignment.CENTER);
        buttonLabel.setText(text);

        Box seperator = new Box();
        seperator.setPadding(Padding.HALF_DEFAULT);

        button.addChild(buttonImage);
        button.addChild(seperator);
        button.addChild(buttonLabel);

        NavigateAction action = new NavigateAction();
        action.setId(url);
        action.setUrl(baseUrl + url);
        List<Action> actions = appInfo.getActions();
        actions.add(action);
        appInfo.setActions(actions);

        container.setOnClickAction(url);
        container.addChild(button);

        return container;
    }
}
