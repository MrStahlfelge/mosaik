package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

/**
 * Copies text into the system clipboard
 */
public class CopyClipboardAction implements Action {
    private String text;

    @Nonnull
    public String getText() {
        if (text == null) {
            throw new IllegalStateException("No text set for " + this.getClass().getSimpleName());
        }

        return text;
    }

    public void setText(@Nonnull String text) {
        this.text = text;
    }
}
