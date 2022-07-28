package org.ergoplatform.mosaik.model.ui.input;

public interface OptionalInputElement<T> extends InputElement<T> {
    boolean isMandatory();

    void setMandatory(boolean mandatory);
}
