package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public interface InputElement<T> {
    @Nullable
    T getValue();

    void setValue(@Nullable T value);

    @Nullable
    Action onValueChangedAction();

    void setOnValueChangedAction(Action action);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
