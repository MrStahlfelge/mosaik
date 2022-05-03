package org.ergoplatform.mosaik.model.ui.layout;

/**
 * Use Column to place items vertically on the screen
 */
public class Column extends LinearLayout<HAlignment> {
    @Override
    protected HAlignment defaultChildAlignment() {
        return HAlignment.CENTER;
    }
}
