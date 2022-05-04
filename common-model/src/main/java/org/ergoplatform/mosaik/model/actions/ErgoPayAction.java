package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Action containing an ErgoPay URL that will make the wallet app switch to ErgoPay.
 * When user navigates back, they will come back to the current {@link org.ergoplatform.mosaik.model.ui.RootView}.
 */
@Since(0)
public class ErgoPayAction extends UrlAction {
    @Nullable private Action onFinished;

    @Nullable
    public Action getOnFinished() {
        return onFinished;
    }

    public void setOnFinished(@Nullable Action onFinished) {
        this.onFinished = onFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErgoPayAction that = (ErgoPayAction) o;
        return Objects.equals(getOnFinished(), that.getOnFinished());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOnFinished());
    }
}
