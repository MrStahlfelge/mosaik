package org.ergoplatform.mosaik.serialization;

import junit.framework.TestCase;

import org.ergoplatform.mosaik.model.ui.LazyLoadBox;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.Column;
import org.ergoplatform.mosaik.model.ui.text.TokenLabel;
import org.junit.Assert;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

public class MosaikSerializerTest extends TestCase {

    public void testJsonRoundTrip() {
        // Add all available elements to a Column
        Column column = new Column();

        for (Class<? extends ViewElement> viewElementClass : findAllViewElements(ViewElement.class.getPackage().getName())) {
            try {
                ViewElement element = viewElementClass.newInstance();

                // add needed properties
                if (element instanceof LazyLoadBox) {
                    ((LazyLoadBox) element).setRequestUrl("...");
                }

                column.addChildren(element);
            } catch (InstantiationException | IllegalAccessException ignored) {
                // abstract classes etc
            }
        }

        String json = MosaikSerializer.toJson(column);
        System.out.println(json);

        ViewElement element = MosaikSerializer.viewElementFromJson(json);
        Assert.assertEquals(column, element);
    }

    public Set<Class<? extends ViewElement>> findAllViewElements(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(ViewElement.class);
    }
}