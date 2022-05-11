package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Action containing a new {@link org.ergoplatform.mosaik.model.ui.ViewElement} to replace the
 * current view's content with.
 * <p>
 * The current content is left with an animation and the user can navigate back.
 */
public class NavigateAction implements Action {
    private ViewElement element;

    @Nonnull
    public ViewElement getElement() {
        if (element == null) {
            throw new IllegalStateException("No view element set for " + this.getClass().getSimpleName());
        }

        return element;
    }

    public void setElement(@Nonnull ViewElement element) {
        Objects.requireNonNull(element);
        this.element = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavigateAction that = (NavigateAction) o;
        return Objects.equals(getElement(), that.getElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElement());
    }
}
