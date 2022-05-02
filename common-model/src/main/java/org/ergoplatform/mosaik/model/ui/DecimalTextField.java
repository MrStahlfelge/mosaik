package org.ergoplatform.mosaik.model.ui;

/**
 * Text field to enter decimal numbers.
 */
public class DecimalTextField extends TextField<Long> {
    private int scale = 9;

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
