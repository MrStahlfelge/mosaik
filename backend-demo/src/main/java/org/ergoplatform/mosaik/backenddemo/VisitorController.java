package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.jackson.MosaikSerializer;
import org.ergoplatform.mosaik.model.FetchActionResponse;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.text.Label;
import org.ergoplatform.mosaik.model.ui.text.LabelStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
public class VisitorController {

    @Autowired
    private MosaikService mosaikService;

    @GetMapping(value = "/visitors/")
    public ModelAndView getVisitorApp(@RequestHeader Map<String, String> headers,
                                      HttpServletRequest request) throws InterruptedException {
        // this deserializes the complete context
        // if you are only interested in certain fields, access header fields directly
        MosaikContext context;
        try {
             context = MosaikSerializer.fromContextHeadersMap(headers);
        } catch (Throwable t) {
            // error getting context -> probably we have a request from a browser
            return new ModelAndView("nobrowser.html");
        }

        // we have different ways to serve the app to the user, not all mutual exclusive:
        // First approach is to serve the app from json templates and do a simple String replace to
        // change content.
        // if all users get the same content, using cached templates is the right thing to do
        // here
        //
        // We are using FreeMarker here to do this
        // https://freemarker.apache.org/docs/dgui_template_overallstructure.html
        // for the following reasons: Caching etc is built in, and it supports templates with list
        // structures that might be useful for real-world apps.
        //
        // the following code is similar to returning a String with @ResponseBody and replacing
        // placeholders like this:
        //return readFile(templates/mainapp.ftl).replace("${hostaddress}", request.getRequestURL().toString()
        // .replace("${appversion}", String.valueOf(APP_VERSION));
        // Additionally, "visitors" is a list so check out how visitorentries.ftl work for this.
        ModelAndView model = new ModelAndView("mainapp");
        model.addObject("hostaddress", request.getRequestURL().toString());
        model.addObject("appversion", String.valueOf(BackendDemoApplication.APP_VERSION));
        model.addObject("visitors", mosaikService.getVisitors(context.guid));

        // this makes the response slow down and is of course only for the demo to emphasize
        // that a server request is running as well as a test case if UI behaves correct when locked
        Thread.sleep(1000);

        return model;

        // alternatively, we could have build the tree of ViewElements here and serialized it
        // this is the right approach when the view tree is highly dynamic or we want to reuse
        // code. See getNextAction() method for an example.

        // As you can see here, we can hold server-side data for the user, based on context.guid
        // We could hold a complete view tree here if needed and change it in memory, but this is
        // only recommended if you don't expect much
        // user inflow as this will consume memory and you need a session invalidation logic.
        // In most cases, you are good to go in storing very few information like user's connected
        // wallet address.


    }

    @PostMapping(value = "visitors/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FetchActionResponse addVisitor(
            @RequestBody Map<String, ?> values,
            @RequestHeader("mosaik-guid") String guid) throws InterruptedException {

        // this makes the response slow down and is of course only for the demo to emphasize
        // that a server request is running as well as a test case if UI behaves correct when locked
        Thread.sleep(1000);

        String userName = (String) values.get("userName");
        List<String> visitors = mosaikService.getVisitors(guid);

        Action nextAction;
        if (userName == null || userName.length() < 3) {
            DialogAction dialogAction = new DialogAction();
            dialogAction.setId("error-noname");
            dialogAction.setMessage("Please enter your name!");
            nextAction = dialogAction;
        } else if (visitors.contains(userName)) {
            // case sensitive for simplicity
            DialogAction dialogAction = new DialogAction();
            dialogAction.setId("error-hasname");
            dialogAction.setMessage("This visitor is already known.");
            nextAction = dialogAction;
        } else {
            visitors.add(userName);
            ChangeSiteAction changeSiteAction = new ChangeSiteAction();
            changeSiteAction.setId("refresh-list");

            // This builds programmatically what is already in template visitorentries.ftl
            // You could use FreeMarker here as well, or you could use the programmatic approach
            // above. For the sake of demonstration, we mixed the approach here.

            Column column = new Column();
            column.setPadding(Padding.DEFAULT);
            // this will make listcolumn to be replaced, the input text field will stay the same
            // to empty the text field after this addition, it had to be replaced as well
            column.setId("listColumn");

            Label title = new Label();
            title.setText("List of last visitors");
            title.setStyle(LabelStyle.BODY1BOLD);

            column.addChild(title);

            for (String visitor : visitors) {
                Label entry = new Label();
                entry.setText(visitor);
                column.addChild(entry);
            }

            changeSiteAction.setNewContent(new ViewContent(column));
            nextAction = changeSiteAction;
        }

        return new FetchActionResponse(BackendDemoApplication.APP_VERSION, nextAction);
    }
}
