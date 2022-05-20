package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.model.FetchActionResponse;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.serialization.MosaikSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MosaikController {
    public static final int APP_VERSION = 1;

    @Autowired
    private MosaikSerializer mosaikSerializer;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView getInitialApp(@RequestHeader Map<String, String> headers,
                                      HttpServletRequest request) throws IOException {
        // this deserializes the complete context
        // if you are only interested in certain fields, access header fields directly
        MosaikContext context = mosaikSerializer.fromContextHeadersMap(headers);

        // we have different ways to serve the app to the user, not all mutual exclusive
        // serve the app from templates and do a simple String replace to change content
        // if all users get the same content, using cached templates is the right thing to do
        // here and that's why it is done here
        // the following code is similar to returning a String with @ResponseBody and replacing
        // placeholders like this:
        //return readFile(templates/mainapp.ftl).replace("${hostaddress}", request.getRequestURL().toString()
        // .replace("${appversion}", String.valueOf(APP_VERSION));
        // However, we are using FreeMarker here
        // https://freemarker.apache.org/docs/dgui_template_overallstructure.html
        // for the following reasons: Caching etc is built in, and it supports templates with list
        // structures that might be useful for real-world apps.
        ModelAndView model = new ModelAndView("mainapp");
        model.addObject("hostaddress", request.getRequestURL().toString());
        model.addObject("appversion", String.valueOf(APP_VERSION));

        return model;

        // alternatively, we could have build the tree of ViewElements here and serialized it
        // this is the right approach when the view tree is highly dynamic

        // we can also initialize hold server-side data for the user, based on context.guid
        // this could be the complete current view tree (only recommended if you don't expect much
        // user inflow as this will consume memory and you need a session invalidation logic)
        // or less information like user's connected wallet


    }

    @PostMapping(value = "/proceed", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getNextAction(
            @RequestBody String jsonBody,
            @RequestHeader("mosaik-guid") String guid) throws InterruptedException {

        // for POST requests from BackendRequestActions, we get the values of input fields
        Map<String, ?> values = mosaikSerializer.getValuesMap(jsonBody);

        // this makes the response slow down and is of course only for the demo to emphasize
        // that a server request is running as well as a test case if UI behaves correct when locked
        Thread.sleep(1000);

        String userName = (String) values.get("userName");
        Action nextAction;
        if (userName == null || userName.length() < 3) {
            DialogAction dialogAction = new DialogAction();
            dialogAction.setId("error-noname");
            dialogAction.setMessage("Please enter your name!");
            nextAction = dialogAction;
        } else {
            DialogAction dialogAction = new DialogAction();
            dialogAction.setId("info-name");
            dialogAction.setMessage(userName + ", what a beautiful name!");
            nextAction = dialogAction;
        }

        FetchActionResponse response = new FetchActionResponse(APP_VERSION, nextAction);

        return mosaikSerializer.toJson(response);
    }
}
