package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ViewContent;
import org.ergoplatform.mosaik.model.actions.BackendRequestAction;
import org.ergoplatform.mosaik.model.ui.layout.Box;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * LazyLoad shows a loading indicator placeholder and fetches a {@link ViewElement} that replaces
 * the LazyLoadBox element when successfully loaded.
 * Best to be used with {@link LoadingIndicator}.
 * <p>
 * In difference to {@link BackendRequestAction}, this one
 * always makes a GET request to the given URL and does not disable user input. It is applicable
 * for fetching informational data that is not mandatory. The request response must be a
 * {@link ViewElement}
 */
@Since(0)
public class LazyLoadBox extends Box {
    private String requestUrl;
    private ViewElement errorView;

    @Nonnull
    public String getRequestUrl() {
        if (requestUrl == null) {
            throw new IllegalStateException("No request url set for " + this.getClass().getSimpleName());
        }
        return requestUrl;
    }

    @Nullable
    public ViewElement getErrorView() {
        return errorView;
    }

    public void setErrorView(ViewElement errorView) {
        this.errorView = errorView;
    }

    public void setRequestUrl(@Nonnull String requestUrl) {
        Objects.requireNonNull(requestUrl);
        this.requestUrl = requestUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LazyLoadBox that = (LazyLoadBox) o;
        return getRequestUrl().equals(that.getRequestUrl()) && Objects.equals(getErrorView(), that.getErrorView());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRequestUrl(), getErrorView());
    }
}
