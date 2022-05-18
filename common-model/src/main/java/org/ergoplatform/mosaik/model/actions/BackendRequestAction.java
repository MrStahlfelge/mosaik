package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

/**
 * Makes a Request to URL. If postDataValues is true, it is a POST request and the body contains all
 * data values of the current view.
 * <p>
 * User data input is disabled while the action runs. If you only want to fetch informational data
 * and don't need to lock the user input, consider using
 * {@link org.ergoplatform.mosaik.model.ui.LazyLoadBox}
 * <p>
 * Response can result in any other action.
 */
@Since(0)
public class BackendRequestAction extends UrlAction {
    private boolean postDataValues = true;

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
        BackendRequestAction that = (BackendRequestAction) o;
        return isPostDataValues() == that.isPostDataValues();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isPostDataValues());
    }
}
