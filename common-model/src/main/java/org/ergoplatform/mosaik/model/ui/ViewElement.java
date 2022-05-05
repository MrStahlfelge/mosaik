package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import java.util.Objects;

import javax.annotation.Nullable;

public abstract class ViewElement implements BaseAttributes {
    private boolean isVisible = true;
    private String elementId;
    private Action onLongPressAction;
    private Action onClickAction;

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Nullable
    @Override
    public String getId() {
        return elementId;
    }

    public void setId(String id) {
        elementId = id;
    }

    @Nullable
    @Override
    public Action getOnLongPressAction() {
        return onLongPressAction;
    }

    @Override
    public void setOnLongPressAction(@Nullable Action action) {
        this.onLongPressAction = action;
    }

    @Nullable
    @Override
    public Action getOnClickAction() {
        return onClickAction;
    }

    @Override
    public void setOnClickAction(@Nullable Action action) {
        onClickAction = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewElement that = (ViewElement) o;
        return isVisible() == that.isVisible() && Objects.equals(elementId, that.elementId) && Objects.equals(getOnLongPressAction(), that.getOnLongPressAction()) && Objects.equals(getOnClickAction(), that.getOnClickAction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isVisible(), elementId, getOnLongPressAction(), getOnClickAction());
    }
}
