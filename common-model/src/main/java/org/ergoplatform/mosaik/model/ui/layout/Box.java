package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Use Box to put elements on top of another
 */
public class Box extends ViewElement implements LayoutElement {
    @Nonnull
    private Padding padding = Padding.NONE;
    private final List<ViewElement> children = new ArrayList<>();
    private final List<HAlignment> childHAlignment = new ArrayList<>();
    private final List<VAlignment> childVAlignment = new ArrayList<>();


    @Override
    @Nonnull
    public Padding getPadding() {
        return padding;
    }

    @Override
    public void setPadding(@Nonnull Padding padding) {
        this.padding = padding;
    }

    @Nonnull
    @Override
    public List<ViewElement> getChildren() {
        return children;
    }

    @Override
    public void addChildren(@Nonnull ViewElement element) {
        addChildren(element, HAlignment.CENTER, VAlignment.CENTER);
    }

    public void addChildren(@Nonnull ViewElement element,
                            @Nonnull HAlignment hAlignment,
                            @Nonnull VAlignment vAlignment) {
        children.add(element);
        childHAlignment.add(hAlignment);
        childVAlignment.add(vAlignment);
    }

    public List<HAlignment> getChildHAlignment() {
        return childHAlignment;
    }

    public List<VAlignment> getChildVAlignment() {
        return childVAlignment;
    }
}
