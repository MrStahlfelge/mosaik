package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

public interface StyleableLabel {
    @Nonnull
    LabelStyle getStyle();

    void setStyle(@Nonnull LabelStyle style);

    @Nonnull
    LabelColor getTextColor();

    void setTextColor(LabelColor color);
}
