package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

/**
 * Action containing a new URL to switch to another Mosaik app.
 * <p>
 * The current content is left with an animation and the user can navigate back.
 *
 * Use this to switch to screens that are self-contained and to which users should be able to
 * navigate to directly.
 */
@Since(0)
public class NavigateAction extends UrlAction {

}
