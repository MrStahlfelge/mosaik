package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nonnull;

/**
 * Label showing an address, providing share/copy and address book functionality
 * (in case application supports it).
 * <p>
 */
public class ErgoAddressLabel extends StyleableTextLabel<String> {
    private boolean expandOnClick = true;

    public ErgoAddressLabel() {
        setTruncationType(TruncationType.MIDDLE);
        setMaxLines(1);
    }

    // TODO default to OpenAddressBookAction


    public boolean isExpandOnClick() {
        return expandOnClick;
    }

    public void setExpandOnClick(boolean expandOnClick) {
        this.expandOnClick = expandOnClick;
    }
}
