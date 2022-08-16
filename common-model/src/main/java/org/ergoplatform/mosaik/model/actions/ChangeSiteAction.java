package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ViewContent;

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
    private ViewContent newContent;
    private String id;

    @Nonnull
    @Override
    public String getId() {
        if (id == null) {
            throw new IllegalStateException("Action id must not be null");
        }

        return id;
    }

    public void setId(@Nonnull String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }


    @Nonnull
    public ViewContent getNewContent() {
        if (newContent == null) {
            throw new IllegalStateException("No view element set for " + this.getClass().getSimpleName());
        }

        return newContent;
    }

    public void setNewContent(@Nonnull ViewContent newContent) {
        Objects.requireNonNull(newContent);
        this.newContent = newContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeSiteAction that = (ChangeSiteAction) o;
        return Objects.equals(getNewContent(), that.getNewContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNewContent());
    }
}
