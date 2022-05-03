package org.ergoplatform.mosaik.model.actions;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Makes a Request to URL. If postDataValues is true, it is a POST request and the body contains all
 * data values of the current {@link org.ergoplatform.mosaik.model.ui.RootView}.
 * <p>
 * User data input is disabled while the action runs. If you only want to fetch informational data
 * and don't need to lock the user input, consider using
 * {@link org.ergoplatform.mosaik.model.ui.LazyLoadBox}
 * <p>
 * Response can result in any other action.
 */
public class ServerRequestAction extends UrlAction {
    private boolean postDataValues;

    public boolean isPostDataValues() {
        return postDataValues;
    }

    public void setPostDataValues(boolean postDataValues) {
        this.postDataValues = postDataValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerRequestAction that = (ServerRequestAction) o;
        return isPostDataValues() == that.isPostDataValues();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isPostDataValues());
    }
}
