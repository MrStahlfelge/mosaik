package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;

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
    private PostValueType postValues = PostValueType.ALL;

    @Nonnull
    public PostValueType getPostValues() {
        return postValues;
    }

    public void setPostValues(@Nonnull PostValueType postValues) {
        Objects.requireNonNull(postValues);
        this.postValues = postValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BackendRequestAction that = (BackendRequestAction) o;
        return getPostValues() == that.getPostValues();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPostValues());
    }

    public enum PostValueType {
        /**
         * sends all data values, enforces validity before backend requests is executed
         */
        ALL,
        /**
         * sends valid data values, ignores invalid data
         */
        VALID,
        /**
         * does not send any data values, ignores invalid data
         */
        NONE
    }
}
