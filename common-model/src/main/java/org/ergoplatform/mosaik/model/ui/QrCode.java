package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.layout.HAlignment;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * shows a QR code
 */
@Since(1)
public class QrCode extends ViewElement implements StringContentElement {
    @Nonnull
    String content = "";

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
        // QrCode has no real alignment, but we return START here so that the serializers omit the field
        return HAlignment.START;
    }

    @Override
    public void setContentAlignment(@Nonnull HAlignment contentAlignment) {
        throw new UnsupportedOperationException("setContentAlignment not supported for QrCode");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QrCode qrCode = (QrCode) o;
        return Objects.equals(getContent(), qrCode.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent());
    }
}
