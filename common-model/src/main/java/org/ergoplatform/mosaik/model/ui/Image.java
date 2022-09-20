package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows an image
 */
@Since(0)
public class Image extends ViewElement {
    @Nullable
    private String url;
    @Nonnull
    private Size size = Size.MEDIUM;

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
    public Size getSize() {
        return size;
    }

    public void setSize(@Nonnull Size size) {
        Objects.requireNonNull(size);
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Image image = (Image) o;
        return Objects.equals(getUrl(), image.getUrl()) && getSize() == image.getSize();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUrl(), getSize());
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE,
        @Since(2)
        XXL
    }
}
