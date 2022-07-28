package org.ergoplatform.mosaik.model.ui;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * A view group can contain many child view elements
 */
public interface ViewGroup {
    /**
     * @return list of child view elements
     */
    @Nonnull
    List<ViewElement> getChildren();

    /**
     * adds a child view element
     */
    void addChild(@Nonnull ViewElement element);

    /**
     * replaces a child element, retaining its layout and position
     */
    void replaceChild(@Nonnull ViewElement elementToReplace, @Nonnull ViewElement newElement);
}
