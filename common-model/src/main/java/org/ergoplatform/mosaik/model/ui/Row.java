package org.ergoplatform.mosaik.model.ui;

/**
 * Use Row to place items horizontally on the screen
 */
public class Row extends LinearLayout<VAlignment> {
    @Override
    protected VAlignment defaultChildAlignment() {
        return VAlignment.CENTER;
    }
}
