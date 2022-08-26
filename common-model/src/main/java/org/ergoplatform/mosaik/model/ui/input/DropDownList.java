package org.ergoplatform.mosaik.model.ui.input;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A dropdown list element where user can select an option from entries key/text map
 */
@Since(0)
public class DropDownList extends ViewElement implements OptionalInputElement<String> {
    @Nullable
    private String value;
    @Nullable
    private String onValueChangedAction;
    private boolean enabled = true;
    @Nonnull
    private LinkedHashMap<String, String> entries = new LinkedHashMap<>();
    private boolean mandatory = true;
    @Nullable
    private String placeholder;

    @Override
    @Nullable
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable String value) {
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

    @Nullable
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(@Nullable String placeholder) {
        this.placeholder = placeholder;
    }

    @Nonnull
    public LinkedHashMap<String, String> getEntries() {
        return entries;
    }

    public void setEntries(@Nonnull LinkedHashMap<String, String> entries) {
        Objects.requireNonNull(entries);
        this.entries = entries;
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
        DropDownList that = (DropDownList) o;
        return isEnabled() == that.isEnabled() && isMandatory() == that.isMandatory() && Objects.equals(getValue(), that.getValue()) && Objects.equals(getOnValueChangedAction(), that.getOnValueChangedAction()) && getEntries().equals(that.getEntries());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), getOnValueChangedAction(), isEnabled(), getEntries(), isMandatory());
    }
}
