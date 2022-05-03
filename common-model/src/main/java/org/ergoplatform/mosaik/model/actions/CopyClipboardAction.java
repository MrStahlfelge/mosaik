package org.ergoplatform.mosaik.model.actions;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CopyClipboardAction that = (CopyClipboardAction) o;
        return Objects.equals(getText(), that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText());
    }
}
