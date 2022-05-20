package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.InitialAppInfo;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Action containing a new {@link InitialAppInfo} to switch to another Mosaik app.
 * <p>
 * The current content is left with an animation and the user can navigate back.
 *
 * Use this to switch to screens that are self-contained and to which users should be able to
 * navigate to directly.
 */
public class NavigateAction implements Action {
    private InitialAppInfo appInfo;
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
    public InitialAppInfo getInitialAppInfo() {
        if (appInfo == null) {
            throw new IllegalStateException("No app info set for " + this.getClass().getSimpleName());
        }

        return appInfo;
    }

    public void setInitialAppInfo(@Nonnull InitialAppInfo element) {
        Objects.requireNonNull(element);
        this.appInfo = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavigateAction that = (NavigateAction) o;
        return Objects.equals(getInitialAppInfo(), that.getInitialAppInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInitialAppInfo());
    }
}
