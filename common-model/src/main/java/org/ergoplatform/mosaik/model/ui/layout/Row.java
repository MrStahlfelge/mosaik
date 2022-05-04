package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;

/**
 * Use Row to place items horizontally on the screen
 */
@Since(0)
public class Row extends LinearLayout<VAlignment> {
    @Override
    public VAlignment defaultChildAlignment() {
        return VAlignment.CENTER;
    }
}
