package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.IconType;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A text input element
 */
public abstract class TextField<T> extends ViewElement implements InputElement<T> {
    @Nullable
    private IconType endIconType;
    @Nullable
    private String onEndIconClicked;
    @Nullable
    private String onImeAction;
    @Nullable
    private String errorMessage;
    @Nullable
    private String placeholder;
    @Nullable
    private T value;
    private long minValue = 0;
    private long maxValue = Long.MAX_VALUE;
    @Nullable
    private String onValueChangedAction;
    private ImeActionType imeActionType = ImeActionType.NEXT;
    private boolean enabled = true;

    @Nullable
    public IconType getEndIcon() {
        return endIconType;
    }

    public void setEndIcon(@Nullable IconType endIconType) {
        this.endIconType = endIconType;
    }

    @Nullable
    public String getOnEndIconClicked() {
        return onEndIconClicked;
    }

    public void setOnEndIconClicked(@Nullable String onEndIconClicked) {
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
    public String getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable String onValueChangedAction) {
        this.onValueChangedAction = onValueChangedAction;
    }

    @Nullable
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(@Nullable String placeholder) {
        this.placeholder = placeholder;
    }

    @Nullable
    public String getOnImeAction() {
        return onImeAction;
    }

    public void setOnImeAction(@Nullable String onImeAction) {
        this.onImeAction = onImeAction;
    }

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    @Nonnull
    public ImeActionType getImeActionType() {
        return imeActionType;
    }

    public void setImeActionType(@Nonnull ImeActionType imeActionType) {
        Objects.requireNonNull(imeActionType);
        this.imeActionType = imeActionType;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextField<?> textField = (TextField<?>) o;
        return getMinValue() == textField.getMinValue() && getMaxValue() == textField.getMaxValue() && isEnabled() == textField.isEnabled() && endIconType == textField.endIconType && Objects.equals(getOnEndIconClicked(), textField.getOnEndIconClicked()) && Objects.equals(getOnImeAction(), textField.getOnImeAction()) && Objects.equals(getErrorMessage(), textField.getErrorMessage()) && Objects.equals(getPlaceholder(), textField.getPlaceholder()) && Objects.equals(getValue(), textField.getValue()) && Objects.equals(getOnValueChangedAction(), textField.getOnValueChangedAction()) && getImeActionType() == textField.getImeActionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), endIconType, getOnEndIconClicked(), getOnImeAction(), getErrorMessage(), getPlaceholder(), getValue(), getMinValue(), getMaxValue(), getOnValueChangedAction(), getImeActionType(), isEnabled());
    }

    public enum ImeActionType {
        NEXT,
        DONE,
        GO,
        SEARCH
    }
}
