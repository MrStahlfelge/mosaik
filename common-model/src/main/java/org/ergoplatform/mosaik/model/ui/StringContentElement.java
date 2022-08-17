package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import javax.annotation.Nonnull;

public interface StringContentElement {
    @Nonnull
    String getContent();

    void setContent(@Nonnull String content);

    @Nonnull
    HAlignment getContentAlignment();

    void setContentAlignment(@Nonnull HAlignment contentAlignment);
}