package org.ergoplatform.mosaik.backenddemo;

import org.ergoplatform.mosaik.model.FetchActionResponse;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.serialization.MosaikSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MosaikRestController {
    @Autowired
    private MosaikSerializer mosaikSerializer;
    @Autowired
    private MosaikService mosaikService;

    @GetMapping("/")
    public String getInitialApp(@RequestHeader Map<String, String> headers,
                                HttpServletRequest request) throws IOException {
        // this deserializes the complete context
        // if you are only interested in certain fields, access header fields directly
        MosaikContext context = mosaikSerializer.fromContextHeadersMap(headers);

        // we have different ways to serve the app to the user, not all mutual exclusive
        // serve the app from templates and do a simple String replace to change content
        // if all users get the same content, caching is the right thing to do here
        // and that's why it is done here
        return mosaikService.getDefaultTree().replace("%HOSTADDRESS%", request.getRequestURL().toString());

        // alternatively, we could have build the tree of ViewElements here and serialized it
        // this is the right approach when the view tree is highly dynamic

        // we can also initialize hold server-side data for the user, based on context.guid
        // this could be the complete current view tree (only recommended if you don't expect much
        // user inflow as this will consume memory and you need a session invalidation logic)
        // or less information like user's connected wallet


    }

    @PostMapping("/")
    public String getNextAction(
            @RequestBody String jsonBody,
            @RequestHeader("mosaik-guid") String guid) {

        // for POST requests from BackendRequestActions, we get the values of input fields
        Map<String, ?> values = mosaikSerializer.getValuesMap(jsonBody);

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

        FetchActionResponse response = new FetchActionResponse(MosaikService.APP_VERSION, nextAction);

        return mosaikSerializer.toJson(response);
    }
}
