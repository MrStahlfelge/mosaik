package org.ergoplatform.mosaik.model.ui.input;

import javax.annotation.Nonnull;

public interface StyleableInputButton<T> extends OptionalInputElement<T> {
    @Nonnull
    InputButtonStyle getStyle();
    void setStyle(@Nonnull InputButtonStyle style);

    enum InputButtonStyle {
        BUTTON_PRIMARY,
        BUTTON_SECONDARY,
        ICON_PRIMARY,
        ICON_SECONDARY,
    }
}
