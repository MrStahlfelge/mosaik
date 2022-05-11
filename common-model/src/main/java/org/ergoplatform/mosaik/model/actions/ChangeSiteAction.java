package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Action containing a new {@link org.ergoplatform.mosaik.model.ui.ViewElement}.
 * The current view's content is replaced by the new
 * element by a difference analysis, thus resulting in scrollbars and element focus remaining.
 * If the new element's id is present in the current view, only that element is replaced. Thus it is
 * possible to change only very few elements of the tree.
 */
@Since(0)
public class ChangeSiteAction implements Action {
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
        ChangeSiteAction that = (ChangeSiteAction) o;
        return Objects.equals(getElement(), that.getElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElement());
    }
}
