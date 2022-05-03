package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nullable;

/**
 * shows a QR code
 */
public class QrCode extends ViewElement {
    @Nullable String content;

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    public enum Size {
        SMALL,
        MEDIUM,
        BIG
    }
}
