package org.ergoplatform.mosaik.model.ui.layout;

import org.ergoplatform.mosaik.model.Since;

/**
 * Use Row to place items horizontally on the screen.
 * By default, Row will fill all available horizontal space. If that is not wanted, set it's
 * `packed` property to `true`
 */
@Since(0)
public class Row extends LinearLayout<VAlignment> {

    private boolean packed = false;

    @Override
    public VAlignment defaultChildAlignment() {
        return VAlignment.CENTER;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }
}
