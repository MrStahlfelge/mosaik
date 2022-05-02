package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

public interface TextLabel<T> extends TextElement<T> {
    /**
     * @return max lines to show, with 0 showing indefinite lines
     */
    int getMaxLines();

    void setMaxLines(int maxLines);

    @Nonnull
    TruncationType getTruncationType();

    void setTruncationType(@Nonnull TruncationType truncationType);

    @Nonnull
    HAlignment getTextAlignment();

    void setTextAlignment(HAlignment textAlignment);
}
