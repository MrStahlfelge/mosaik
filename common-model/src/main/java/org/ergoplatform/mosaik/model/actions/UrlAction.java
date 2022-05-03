package org.ergoplatform.mosaik.model.actions;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UrlAction implements Action {
    private String url;

    @Nonnull
    public String getUrl() {
        if (url == null) {
            throw new IllegalStateException("No url set for " + this.getClass().getSimpleName());
        }

        return url;
    }

    public void setUrl(@Nonnull String url) {
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
