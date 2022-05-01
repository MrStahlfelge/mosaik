package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

public interface LayoutElement extends ViewGroup {
    @Nonnull
    Padding getPadding();

    void setPadding(@Nonnull Padding padding);
}
