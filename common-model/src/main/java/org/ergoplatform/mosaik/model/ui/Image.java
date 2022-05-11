package org.ergoplatform.mosaik.model.ui;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows an image
 */
public class Image extends ViewElement {
    @Nullable
    private String url;
    @Nonnull
    private Size iconSize = Size.MEDIUM;

    @Nonnull
    public String getUrl() {
        if (url == null) {
            throw new IllegalStateException("No image url set for " + this.getClass().getSimpleName());
        }

        return url;
    }

    public void setUrl(@Nonnull String url) {
        Objects.requireNonNull(url);
        this.url = url;
    }

    @Nonnull
    public Size getIconSize() {
        return iconSize;
    }

    public void setIconSize(@Nonnull Size iconSize) {
        Objects.requireNonNull(iconSize);
        this.iconSize = iconSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Image image = (Image) o;
        return Objects.equals(getUrl(), image.getUrl()) && getIconSize() == image.getIconSize();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUrl(), getIconSize());
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}
