package org.ergoplatform.mosaik.model.ui.layout;

/**
 * Use Row to place items horizontally on the screen
 */
public class Row extends LinearLayout<VAlignment> {
    @Override
    protected VAlignment defaultChildAlignment() {
        return VAlignment.CENTER;
    }
}
