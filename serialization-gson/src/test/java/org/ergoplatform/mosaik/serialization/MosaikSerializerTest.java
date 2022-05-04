package org.ergoplatform.mosaik.serialization;

import junit.framework.TestCase;

import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.ValidationException;
import net.jimblackler.jsonschemafriend.Validator;

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

    public void testJsonRoundTrip() throws GenerationException, ValidationException {
        // collect available actions
        LinkedList<Action> actions = new LinkedList<>();
        for (Class<? extends Action> actionClass : findAllActions(Action.class.getPackage().getName())) {
            try {
                Action action = actionClass.newInstance();

                // add needed properties
                if (action instanceof UrlAction) {
                    ((UrlAction) action).setUrl("url");
                } else if (action instanceof DialogAction) {
                    ((DialogAction) action).setMessage("message");
                } else if (action instanceof ChangeSiteAction) {
                    ((ChangeSiteAction) action).setElement(new Box());
                } else if (action instanceof NavigateAction) {
                    ((NavigateAction) action).setElement(new Box());
                } else if (action instanceof CopyClipboardAction) {
                    ((CopyClipboardAction) action).setText("text");
                }

                actions.add(action);
            } catch (InstantiationException | IllegalAccessException ignored) {
                // abstract classes etc
            }
        }


        // Add all available view elements to a Column
        Column column = new Column();

        for (Class<? extends ViewElement> viewElementClass : findAllViewElements(ViewElement.class.getPackage().getName())) {
            try {
                ViewElement element = viewElementClass.newInstance();

                // add needed properties
                if (element instanceof LazyLoadBox) {
                    ((LazyLoadBox) element).setRequestUrl("...");
                } else if (element instanceof TokenLabel) {
                    ((TokenLabel) element).setTokenId("tokenid");
                }

                // add actions from queue
                if (!actions.isEmpty()) {
                    try {
                        element.setOnClickAction(actions.getFirst());
                        actions.removeFirst();
                    } catch (IllegalArgumentException e) {
                        // some elements don't let set actions
                    }
                }

                // TODO column.addChild(element);
            } catch (InstantiationException | IllegalAccessException ignored) {
                // abstract classes etc
            }
        }

        column.addChild(new Box(), HAlignment.END, 2);

        String json = new MosaikSerializer().toJson(column);
        System.out.println(json);

        SchemaStore schemaStore = new SchemaStore(); // Initialize a SchemaStore.
        // Load the schema.
        Schema schema = schemaStore.loadSchema(MosaikSerializerTest.class.getResource("/schema/viewelement.json"));
        Validator validator = new Validator(); // Create a validator.
        validator.validateJson(schema, json);

        ViewElement element = new MosaikSerializer().viewElementFromJson(json);
        Assert.assertEquals(column, element);
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