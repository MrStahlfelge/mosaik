package org.ergoplatform.mosaik.model.ui;

/**
 * Use Column to place items vertically on the screen
 */
public class Column extends LinearLayout<HAlignment> {
    @Override
    protected HAlignment defaultChildAlignment() {
        return HAlignment.CENTER;
    }
}
