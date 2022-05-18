package org.ergoplatform.mosaik.model;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nonnull;

/**
 * Response to subsequent backend requests triggered by a
 * {@link org.ergoplatform.mosaik.model.actions.BackendRequestAction}
 */
public class FetchActionResponse {
    private int appVersion;
    private Action action;

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    @Nonnull
    public Action getAction() {
        if (action == null) {
            throw new IllegalStateException("action must not be null");
        }

        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
