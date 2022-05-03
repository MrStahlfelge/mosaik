package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nullable;

/**
 * Action containing an ErgoPay URL that will make the wallet app switch to ErgoPay.
 * When user navigates back, they will come back to the current {@link org.ergoplatform.mosaik.model.ui.RootView}.
 */
public class ErgoAuthAction extends UrlAction {
    @Nullable
    private Action onFinished;

    @Nullable
    public Action getOnFinished() {
        return onFinished;
    }

    public void setOnFinished(@Nullable Action onFinished) {
        this.onFinished = onFinished;
    }
}
