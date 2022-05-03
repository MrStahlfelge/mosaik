package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.ViewElement;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows text
 */
public class StyleableTextLabel<T> extends ViewElement implements StyleableLabel, TextLabel<T> {
    @Nonnull
    private LabelStyle style = LabelStyle.BODY1;
    @Nonnull
    private ForegroundColor textColor = ForegroundColor.DEFAULT;
    private T text;
    private int maxLines = 0;
    @Nonnull
    private TruncationType truncationType = TruncationType.END;
    @Nonnull
    private HAlignment textAlignment = HAlignment.START;

    @Nonnull
    @Override
    public LabelStyle getStyle() {
        return style;
    }

    @Override
    public void setStyle(@Nonnull LabelStyle style) {
        this.style = style;
    }

    @Override
    @Nonnull
    public ForegroundColor getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(@Nonnull ForegroundColor textColor) {
        this.textColor = textColor;
    }

    @Nullable
    @Override
    public T getText() {
        return text;
    }

    @Override
    public void setText(@Nullable T text) {
        this.text = text;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    @Nonnull
    public TruncationType getTruncationType() {
        return truncationType;
    }

    @Override
    public void setTruncationType(@Nonnull TruncationType truncationType) {
        this.truncationType = truncationType;
    }

    @Override
    @Nonnull
    public HAlignment getTextAlignment() {
        return textAlignment;
    }

    @Override
    public void setTextAlignment(@Nonnull HAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }
}
