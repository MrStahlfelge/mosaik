package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Button extends ViewElement implements TextLabel<String> {
    @Nullable private String text;
    private int maxLines;
    @Nonnull private TruncationType truncationType = TruncationType.END;
    @Nonnull private HAlignment textAlignment = HAlignment.CENTER;

    @Override
    @Nullable
    public String getText() {
        return text;
    }

    @Override
    public void setText(@Nullable String text) {
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
