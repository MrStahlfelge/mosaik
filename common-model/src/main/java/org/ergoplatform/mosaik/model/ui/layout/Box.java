package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void addChild(@Nonnull ViewElement element) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Box box = (Box) o;
        return getPadding() == box.getPadding() && getChildren().equals(box.getChildren()) && getChildHAlignment().equals(box.getChildHAlignment()) && getChildVAlignment().equals(box.getChildVAlignment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPadding(), getChildren(), getChildHAlignment(), getChildVAlignment());
    }
}
