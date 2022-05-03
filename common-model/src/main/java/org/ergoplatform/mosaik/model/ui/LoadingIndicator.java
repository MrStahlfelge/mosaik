package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows a spinner/loading indicator
 */
public class LoadingIndicator extends ViewElement {
    @Nonnull private Size size = Size.SMALL;

    @Nonnull
    public Size getSize() {
        return size;
    }

    public void setSize(@Nonnull Size size) {
        this.size = size;
    }

    @Override
    public void setOnClickAction(@Nullable Action action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public Action getOnLongPressAction() {
        throw new IllegalArgumentException("OnOnLongPressAction can't be set for" +
                this.getClass().getSimpleName());
    }

    public enum Size {
        SMALL,
        MEDIUM
    }
}
