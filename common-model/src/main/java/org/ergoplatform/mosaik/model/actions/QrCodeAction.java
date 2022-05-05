package org.ergoplatform.mosaik.model.actions;

/**
 * Scans a QR code and replaces a {@link org.ergoplatform.mosaik.model.ui.ViewElement} of
 * the current view with a given
 * {@link org.ergoplatform.mosaik.model.ui.input.InputElement}. Similar to {@link ChangeSiteAction},
 * the new element's id must match the id of an id in the current view. If the new element is
 * a {@link org.ergoplatform.mosaik.model.ui.input.InputElement}, its value is set to the scanned
 * value.
 */
public class QrCodeAction extends ChangeSiteAction {

}
