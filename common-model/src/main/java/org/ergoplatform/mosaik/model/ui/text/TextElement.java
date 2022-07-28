package org.ergoplatform.mosaik.model.ui.text;

import javax.annotation.Nullable;

public interface TextElement<T> {
    @Nullable
    T getText();

    void setText(@Nullable T text);
}
