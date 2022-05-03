package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * LinearLayout places items stacked on the screen
 */
public abstract class LinearLayout<CSA> extends ViewElement implements LayoutElement {
    @Nonnull
    private Padding padding = Padding.NONE;
    private final List<ViewElement> children = new ArrayList<>();
    private final List<CSA> childAlignment = new ArrayList<>();
    private final List<Integer> childWeight = new ArrayList<>();

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
        addChildren(element, defaultChildAlignment(), 1);
    }

    protected abstract CSA defaultChildAlignment();

    public void addChildren(@Nonnull ViewElement element,
                            @Nonnull CSA hAlignment,
                            int childWeight) {
        children.add(element);
        childAlignment.add(hAlignment);
        this.childWeight.add(childWeight);
    }

    private int getChildPos(ViewElement element) {
        return children.indexOf(element);
    }

    @Nonnull
    public CSA getChildAlignment(@Nonnull ViewElement element) {
        int pos = getChildPos(element);
        return childAlignment.get(pos);
    }

    public int getChildWeight(@Nonnull ViewElement element) {
        int pos = getChildPos(element);
        return childWeight.get(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LinearLayout<?> that = (LinearLayout<?>) o;
        return getPadding() == that.getPadding() && getChildren().equals(that.getChildren()) && childAlignment.equals(that.childAlignment) && childWeight.equals(that.childWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPadding(), getChildren(), childAlignment, childWeight);
    }
}
