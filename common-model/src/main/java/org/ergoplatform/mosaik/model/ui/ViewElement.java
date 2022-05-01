package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public abstract class ViewElement implements BaseAttributes {
    private ViewElement parent;
    private boolean isVisible = true;
    private String elementId;
    private Action onLongPressAction;
    private Action onClickAction;

    @Nullable
    public ViewElement getParent() {
        return parent;
    }

    public void setParent(@Nullable ViewElement parent) {
        this.parent = parent;
    }

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
}
