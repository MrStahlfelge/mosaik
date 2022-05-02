package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

/**
 * Copies text into the system clipboard
 */
public class CopyClipboardAction extends Action {
    @Nonnull private final String text;

    public CopyClipboardAction(@Nonnull String text) {
        this.text = text;
    }
}
