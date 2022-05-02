package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

/**
 * ViewGroup supporting laying out the children
 */
public interface LayoutElement extends ViewGroup {
    /**
     * @return padding this layout element uses
     */
    @Nonnull
    Padding getPadding();

    /**
     * sets the {@link Padding} for this layout element
     */
    void setPadding(@Nonnull Padding padding);
}
