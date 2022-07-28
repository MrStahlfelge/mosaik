package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;

import javax.annotation.Nonnull;

public interface StyleableLabel {
    @Nonnull
    LabelStyle getStyle();

    void setStyle(@Nonnull LabelStyle style);

    @Nonnull
    ForegroundColor getTextColor();

    void setTextColor(ForegroundColor color);
}
