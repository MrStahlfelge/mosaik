package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * shows markdown content - supported features depend on Mosaik executor
 */
@Since(1)
public class MarkDown extends ViewElement implements StringContentElement {
    @Nonnull
    String content = "";
    @Nonnull
    private HAlignment contentAlignment = HAlignment.START;

    @Nonnull
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(@Nonnull String content) {
        this.content = content;
    }

    @Nonnull
    @Override
    public HAlignment getContentAlignment() {
        return contentAlignment;
    }

    @Override
    public void setContentAlignment(@Nonnull HAlignment contentAlignment) {
        this.contentAlignment = contentAlignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MarkDown that = (MarkDown) o;
        return Objects.equals(getContent(), that.getContent()) && getContentAlignment() == that.getContentAlignment();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent(), getContentAlignment());
    }
}
