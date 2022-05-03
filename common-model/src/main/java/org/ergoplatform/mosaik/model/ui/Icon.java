package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

/**
 * Shows an icon
 */
public class Icon extends ViewElement {
    @Nonnull
    private IconType iconType = IconType.INFO;
    @Nonnull
    private Size iconSize = Size.SMALL;
    @Nonnull
    private ForegroundColor tintColor = ForegroundColor.DEFAULT;

    @Nonnull
    public IconType getIconType() {
        return iconType;
    }

    public void setIconType(@Nonnull IconType iconType) {
        this.iconType = iconType;
    }

    @Nonnull
    public Size getIconSize() {
        return iconSize;
    }

    public void setIconSize(@Nonnull Size iconSize) {
        this.iconSize = iconSize;
    }

    @Nonnull
    public ForegroundColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(@Nonnull ForegroundColor tintColor) {
        this.tintColor = tintColor;
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}
