package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public abstract class TextField<T> extends ViewElement implements InputElement<T> {
    @Nullable
    private Icon endIcon;
    @Nullable
    private Action onEndIconClicked;
    @Nullable
    private String errorMessage;

    @Nullable
    public Icon getEndIcon() {
        return endIcon;
    }

    public void setEndIcon(@Nullable Icon endIcon) {
        this.endIcon = endIcon;
    }

    @Nullable
    public Action getOnEndIconClicked() {
        return onEndIconClicked;
    }

    public void setOnEndIconClicked(@Nullable Action onEndIconClicked) {
        this.onEndIconClicked = onEndIconClicked;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
