package org.ergoplatform.mosaik.model.ui.text;

/**
 * An element that is collapsed but can be expanded on click
 */
public interface ExpandableElement {
    boolean isExpandOnClick();
    void setExpandOnClick(boolean expandable);
}
