package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

/**
 * A text input element
 */
public abstract class TextField<T> extends ViewElement implements InputElement<T> {
    @Nullable
    private IconType endIconType;
    @Nullable
    private Action onEndIconClicked;
    @Nullable
    private String errorMessage;
    @Nullable
    private T value;
    @Nullable
    private Action onValueChangedAction;
    private boolean enabled = true;

    @Nullable
    public IconType getEndIcon() {
        return endIconType;
    }

    public void setEndIcon(@Nullable IconType endIconType) {
        this.endIconType = endIconType;
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

    @Override
    @Nullable
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable T value) {
        this.value = value;
    }

    @Override
    @Nullable
    public Action getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable Action onValueChangedAction) {
        this.onValueChangedAction = onValueChangedAction;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
