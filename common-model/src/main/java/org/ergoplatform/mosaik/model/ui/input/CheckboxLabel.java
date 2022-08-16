package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.text.Label;

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Label showing a checkbox/switch. A value of null means the box is neither checked nor not checked
 */
@Since(0)
public class CheckboxLabel extends Label implements OptionalInputElement<Boolean> {
    @Nullable
    private Boolean value;
    @Nullable
    private String onValueChangedAction;
    private boolean enabled = true;
    private boolean mandatory = true;

    @Override
    public void setOnClickAction(@Nullable String action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    @Nullable
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable Boolean value) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckboxLabel that = (CheckboxLabel) o;
        return isEnabled() == that.isEnabled() && isMandatory() == that.isMandatory() && Objects.equals(getValue(), that.getValue()) && Objects.equals(getOnValueChangedAction(), that.getOnValueChangedAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), getOnValueChangedAction(), isEnabled(), isMandatory());
    }
}
