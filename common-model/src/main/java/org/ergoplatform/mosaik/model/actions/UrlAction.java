package org.ergoplatform.mosaik.model.actions;

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
}
