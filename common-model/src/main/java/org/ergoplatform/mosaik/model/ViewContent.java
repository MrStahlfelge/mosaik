package org.ergoplatform.mosaik.model;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Root view element with list of actions
 */
public class ViewContent {
    private List<Action> actions;
    private ViewElement view;

    public ViewContent() {
    }

    public ViewContent(ViewElement view) {
        this.view = view;
    }

    public ViewContent(List<Action> actions, ViewElement view) {
        this.view = view;
        this.actions = actions;
    }

    @Nonnull
    public List<Action> getActions() {
        return actions != null ? actions : new LinkedList<Action>();
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Nonnull
    public ViewElement getView() {
        if (view == null) {
            throw new IllegalStateException("ViewContent's view must not be null");
        }

        return view;
    }

    public void setView(@Nonnull ViewElement view) {
        Objects.requireNonNull(view);
        this.view = view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewContent that = (ViewContent) o;
        return getActions().equals(that.getActions()) && getView().equals(that.getView());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActions(), getView());
    }
}
