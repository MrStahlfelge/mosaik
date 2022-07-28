package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Copies text into the system clipboard
 */
@Since(0)
public class CopyClipboardAction implements Action {
    private String text;
    private String id;

    @Nonnull
    @Override
    public String getId() {
        if (id == null) {
            throw new IllegalStateException("Action id must not be null");
        }

        return id;
    }

    public void setId(@Nonnull String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    @Nonnull
    public String getText() {
        if (text == null) {
            throw new IllegalStateException("No text set for " + this.getClass().getSimpleName());
        }

        return text;
    }

    public void setText(@Nonnull String text) {
        Objects.requireNonNull(text);
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
