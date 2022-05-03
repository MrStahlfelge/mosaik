package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.ui.ViewElement;

import javax.annotation.Nonnull;

/**
 * Action containing a new {@link org.ergoplatform.mosaik.model.ui.ViewElement} to replace the
 * current {@link org.ergoplatform.mosaik.model.ui.RootView}'s content with.
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
        this.element = element;
    }
}
