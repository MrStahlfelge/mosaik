package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Button type element to let the user choose a single of his addresses.
 */
@Since(0)
public class ErgoAddressChooseButton extends ViewElement implements StyleableInputButton<String> {
    @Nullable
    private String address;
    @Nullable
    private String onValueChangedAction;
    private boolean enabled = true;
    private boolean mandatory = true;
    @Nonnull
    private StyleableInputButton.InputButtonStyle style = StyleableInputButton.InputButtonStyle.BUTTON_PRIMARY;

    @Nullable
    @Override
    public String getValue() {
        return address;
    }

    @Override
    public void setValue(@Nullable String value) {
        address = value;
    }

    @Nullable
    @Override
    public String getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable String action) {
        onValueChangedAction = action;
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
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Nonnull
    @Override
    @Since(1)
    public StyleableInputButton.InputButtonStyle getStyle() {
        return style;
    }

    @Override
    @Since(1)
    public void setStyle(@Nonnull StyleableInputButton.InputButtonStyle style) {
        Objects.requireNonNull(style);
        this.style = style;
    }

    @Override
    public void setOnClickAction(@Nullable String action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErgoAddressChooseButton that = (ErgoAddressChooseButton) o;
        return isMandatory() == that.isMandatory() && isEnabled() == that.isEnabled() && Objects.equals(address, that.address) && Objects.equals(getOnValueChangedAction(), that.getOnValueChangedAction()) && Objects.equals(getStyle(), that.getStyle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, getOnValueChangedAction(), isEnabled(), isMandatory(), getStyle());
    }
}
