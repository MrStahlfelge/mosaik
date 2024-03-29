package org.ergoplatform.mosaik.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;

import junit.framework.TestCase;

import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.ValidationException;
import net.jimblackler.jsonschemafriend.Validator;

import org.ergoplatform.mosaik.model.MosaikApp;
import org.ergoplatform.mosaik.model.MosaikContext;
import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction;
import org.ergoplatform.mosaik.model.actions.CopyClipboardAction;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.model.actions.UrlAction;
import org.ergoplatform.mosaik.model.ui.Image;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.QrCode;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.input.DropDownList;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.layout.Padding;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;
import org.junit.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class MosaikSerializerTest extends TestCase {

    public static int TEST_MOSAIK_VERSION = MosaikContext.LIBRARY_MOSAIK_VERSION;

    public void testJsonRoundTrip() throws GenerationException, ValidationException, JsonProcessingException {
        // collect available actions
        LinkedList<Action> actions = new LinkedList<>();
        for (Class<? extends Action> actionClass : findAllActions(Action.class.getPackage().getName())) {
            Since annotation = actionClass.getAnnotation(Since.class);
            int sinceVersion = annotation != null ? annotation.value() : (TEST_MOSAIK_VERSION + 1);

            if (sinceVersion <= TEST_MOSAIK_VERSION) {
                try {
                    Action action = actionClass.newInstance();

                    // add needed properties
                    if (action instanceof UrlAction) {
                        ((UrlAction) action).setUrl("url");
                    } else if (action instanceof DialogAction) {
                        ((DialogAction) action).setMessage("message");
                    } else if (action instanceof ChangeSiteAction) {
                        ((ChangeSiteAction) action).setNewContent(new ViewContent(new ArrayList<>(actions), new Box()));
                    } else if (action instanceof CopyClipboardAction) {
                        ((CopyClipboardAction) action).setText("text");
                    }
                    action.setId("action_" + actions.size());
                    actions.add(action);
                } catch (InstantiationException | IllegalAccessException ignored) {
                    // abstract classes etc
                }
            }
        }


        // Add all available view elements to a Column
        Column column = new Column();
        int actionIdx = 0;

        for (Class<? extends ViewElement> viewElementClass : findAllViewElements(ViewElement.class.getPackage().getName())) {
            Since annotation = viewElementClass.getAnnotation(Since.class);
            int sinceVersion = annotation != null ? annotation.value() : (TEST_MOSAIK_VERSION + 1);

            if (sinceVersion <= TEST_MOSAIK_VERSION) {
                try {
                    ViewElement element = viewElementClass.newInstance();

                    // add needed properties
                    if (element instanceof LazyLoadBox) {
                        ((LazyLoadBox) element).setRequestUrl("...");
                        ((LazyLoadBox) element).setErrorView(new Box());
                    } else if (element instanceof TokenLabel) {
                        ((TokenLabel) element).setTokenId("tokenid");
                    } else if (element instanceof Image) {
                        ((Image) element).setUrl("url");
                    } else if (element instanceof QrCode) {
                        ((QrCode) element).setContent("qrcodecontent");
                    } else if (element instanceof DropDownList) {
                        ((DropDownList) element).getEntries().put("id", "text");
                    }

                    // add actions from queue
                    if (actionIdx < actions.size()) {
                        try {
                            element.setOnClickAction("action_" + actionIdx);
                            actionIdx++;
                        } catch (IllegalArgumentException e) {
                            // some elements don't let set actions
                        }
                    }

                    column.addChild(element);
                } catch (InstantiationException | IllegalAccessException ignored) {
                    // abstract classes etc
                }
            }
        }

        Column chilAlignmentColumn = new Column();
        chilAlignmentColumn.setAlignmentForAdded(HAlignment.JUSTIFY);
        column.addChild(chilAlignmentColumn, HAlignment.END, 2);
        column.setSpacing(Padding.DEFAULT);

        MosaikApp content = new MosaikApp();
        content.setView(column);
        content.setActions(actions);
        content.setManifest(new MosaikManifest(
                "appname",
                0,
                MosaikContext.LIBRARY_MOSAIK_VERSION,
                null,
                0
        ));

        String jsonFromGson = new MosaikSerializer().toJson(content);
        System.out.println(jsonFromGson);
        // now with jackson
        String jsonFromJackson = serializeWithJackson(content);
        System.out.println(jsonFromJackson);

        SchemaStore schemaStore = new SchemaStore(); // Initialize a SchemaStore.
        // Load the schema.
        Schema schema = schemaStore.loadSchema(MosaikSerializerTest.class.getResource("/schema/viewcontent.json"));
        Validator validator = new Validator(); // Create a validator.
        validator.validateJson(schema, jsonFromGson);
        validator.validateJson(schema, jsonFromJackson);

        ViewContent content2 = new MosaikSerializer().viewContentFromJson(jsonFromGson);
        ViewContent content3 = new MosaikSerializer().viewContentFromJson(jsonFromJackson);
        Assert.assertEquals(column, content2.getView());
        Assert.assertEquals(column, content3.getView());
        Assert.assertTrue(actions.size() == content2.getActions().size() && actions.containsAll(content2.getActions()) && content2.getActions().containsAll(actions));
    }

    private String serializeWithJackson(MosaikApp content) throws JsonProcessingException {
        return org.ergoplatform.mosaik.jackson.MosaikSerializer.getMosaikMapper().writeValueAsString(content);
    }

    public Set<Class<? extends ViewElement>> findAllViewElements(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(ViewElement.class);
    }

    public Set<Class<? extends Action>> findAllActions(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Action.class);
    }
}