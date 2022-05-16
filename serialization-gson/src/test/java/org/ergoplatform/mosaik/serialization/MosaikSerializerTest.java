package org.ergoplatform.mosaik.serialization;

import junit.framework.TestCase;

import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.ValidationException;
import net.jimblackler.jsonschemafriend.Validator;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.ChangeSiteAction;
import org.ergoplatform.mosaik.model.actions.CopyClipboardAction;
import org.ergoplatform.mosaik.model.actions.DialogAction;
import org.ergoplatform.mosaik.model.actions.NavigateAction;
import org.ergoplatform.mosaik.model.actions.UrlAction;
import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.Box;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;
import org.junit.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.LinkedList;
import java.util.Set;

public class MosaikSerializerTest extends TestCase {

    public static int TEST_MOSAIK_VERSION = 0;

    public void testJsonRoundTrip() throws GenerationException, ValidationException {
        // collect available actions
        LinkedList<Action> actions = new LinkedList<>();
        for (Class<? extends Action> actionClass : findAllActions(Action.class.getPackage().getName())) {
            Since annotation = actionClass.getAnnotation(Since.class);
            int sinceVersion = annotation != null ? annotation.value() : 1;

            if (sinceVersion <= TEST_MOSAIK_VERSION) {
                try {
                    Action action = actionClass.newInstance();

                    // add needed properties
                    if (action instanceof UrlAction) {
                        ((UrlAction) action).setUrl("url");
                    } else if (action instanceof DialogAction) {
                        ((DialogAction) action).setMessage("message");
                    } else if (action instanceof ChangeSiteAction) {
                        ((ChangeSiteAction) action).setNewContent(new ViewContent(new Box()));
                    } else if (action instanceof NavigateAction) {
                        ((NavigateAction) action).setElement(new Box());
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
            int sinceVersion = annotation != null ? annotation.value() : 1;

            if (sinceVersion <= TEST_MOSAIK_VERSION) {
                try {
                    ViewElement element = viewElementClass.newInstance();

                    // add needed properties
                    if (element instanceof LazyLoadBox) {
                        ((LazyLoadBox) element).setRequestUrl("...");
                    } else if (element instanceof TokenLabel) {
                        ((TokenLabel) element).setTokenId("tokenid");
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

        column.addChild(new Box(), HAlignment.END, 2);

        ViewContent content = new ViewContent(column);
        content.setActions(actions);

        String json = new MosaikSerializer().toJson(content);
        System.out.println(json);

        SchemaStore schemaStore = new SchemaStore(); // Initialize a SchemaStore.
        // Load the schema.
        Schema schema = schemaStore.loadSchema(MosaikSerializerTest.class.getResource("/schema/viewcontent.json"));
        Validator validator = new Validator(); // Create a validator.
        validator.validateJson(schema, json);

        ViewContent content2 = new MosaikSerializer().viewElementFromJson(json);
        Assert.assertEquals(column, content2.getView());
        Assert.assertTrue(actions.size() == content2.getActions().size() && actions.containsAll(content2.getActions()) && content2.getActions().containsAll(actions));
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