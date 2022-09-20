package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Grid is a responsive element presenting child elements in a grid-style layout. The number of
 * columns is determined by the min element width.
 */
@Since(2)
public class Grid extends ViewElement implements LayoutElement {
    @Nonnull
    private Padding padding = Padding.NONE;
    private final List<ViewElement> children = new ArrayList<>();
    @Nonnull
    ElementSize elementSize = ElementSize.SMALL;

    @Nonnull
    public ElementSize getElementSize() {
        return elementSize;
    }

    public void setElementSize(@Nonnull ElementSize elementSize) {
        Objects.requireNonNull(elementSize);
        this.elementSize = elementSize;
    }

    @Override
    public void addChild(@Nonnull ViewElement element) {
        Objects.requireNonNull(element);
        children.add(element);
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

    public enum ElementSize {
        /**
         * Might display a two-column layout on bigger phones
         */
        MIN, // 240dp
        /**
         * Will display a one-column layout on nearly all phones
         */
        SMALL, // 320 dp
        MEDIUM, // 420 dp
        LARGE // 520 dp
    }
}
