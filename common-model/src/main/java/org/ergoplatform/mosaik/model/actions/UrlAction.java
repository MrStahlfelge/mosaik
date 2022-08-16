package org.ergoplatform.mosaik.model.actions;

import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class UrlAction implements Action {
    private String url;
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
    public String getUrl() {
        if (url == null) {
            throw new IllegalStateException("No url set for " + this.getClass().getSimpleName());
        }

        return url;
    }

    public void setUrl(@Nonnull String url) {
        Objects.requireNonNull(url);
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlAction urlAction = (UrlAction) o;
        return Objects.equals(getUrl(), urlAction.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl());
    }
}
