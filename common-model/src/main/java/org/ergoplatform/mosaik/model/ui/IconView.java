package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

public class IconView extends ViewElement {
    @Nonnull
    private Icon iconType = Icon.INFO;
    @Nonnull
    private Size iconSize = Size.SMALL;
    @Nonnull
    private LabelColor tintColor = LabelColor.DEFAULT;

    @Nonnull
    public Icon getIconType() {
        return iconType;
    }

    public void setIconType(@Nonnull Icon iconType) {
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
    public LabelColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(@Nonnull LabelColor tintColor) {
        this.tintColor = tintColor;
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }
}
