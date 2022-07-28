package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Use Box to put elements on top of another
 */
@Since(0)
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
        Objects.requireNonNull(padding);
        this.padding = padding;
    }

    @Nonnull
    @Override
    public List<ViewElement> getChildren() {
        return children;
    }

    @Override
    public void addChild(@Nonnull ViewElement element) {
        addChild(element, HAlignment.CENTER, VAlignment.CENTER);
    }

    public void addChild(@Nonnull ViewElement element,
                         @Nonnull HAlignment hAlignment,
                         @Nonnull VAlignment vAlignment) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(hAlignment);
        Objects.requireNonNull(vAlignment);
        children.add(element);
        childHAlignment.add(hAlignment);
        childVAlignment.add(vAlignment);
    }

    @Override
    public void replaceChild(@Nonnull ViewElement elementToReplace, @Nonnull ViewElement newElement) {
        Objects.requireNonNull(elementToReplace);
        Objects.requireNonNull(newElement);
        children.set(getChildPos(elementToReplace), newElement);
    }

    private int getChildPos(ViewElement element) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == element)
                return i;
        }
        return -1;
    }

    @Nonnull
    public HAlignment getChildHAlignment(@Nonnull ViewElement element) {
        return childHAlignment.get(getChildPos(element));
    }

    @Nonnull
    public VAlignment getChildVAlignment(@Nonnull ViewElement element) {
        return childVAlignment.get(getChildPos(element));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Box box = (Box) o;
        return getPadding() == box.getPadding() && getChildren().equals(box.getChildren()) && childHAlignment.equals(box.childHAlignment) && childVAlignment.equals(box.childVAlignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPadding(), getChildren(), childHAlignment, childVAlignment);
    }
}
