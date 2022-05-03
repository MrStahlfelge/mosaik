package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

/**
 * Makes a Request to URL. If postDataValues is true, it is a POST request and the body contains all
 * data values of the current {@link org.ergoplatform.mosaik.model.ui.RootView}.
 * <p>
 * User data input is disabled while the action runs. If you only want to fetch informational data
 * and don't need to lock the user input, consider using
 * {@link org.ergoplatform.mosaik.model.ui.LazyLoadBox}
 * <p>
 * Response can result in any other action.
 */
public class ServerRequestAction extends UrlAction {
    private boolean postDataValues;

    public boolean isPostDataValues() {
        return postDataValues;
    }

    public void setPostDataValues(boolean postDataValues) {
        this.postDataValues = postDataValues;
    }
}
