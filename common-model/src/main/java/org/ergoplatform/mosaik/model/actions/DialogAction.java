package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows an error or info dialog that is shown modal on top of the current view.
 */
public class DialogAction implements Action {
    String message;
    @Nullable String positiveButtonText;
    @Nullable String negativeButtonText;
    @Nullable Action onPositiveButtonClicked;
    @Nullable Action onNegativeButtonClicked;

    @Nonnull
    public String getMessage() {
        if (message == null) {
            throw new IllegalStateException("No message text set for " + this.getClass().getSimpleName());
        }

        return message;
    }

    public void setMessage(@Nonnull String message) {
        this.message = message;
    }

    @Nullable
    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public void setPositiveButtonText(@Nullable String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    @Nullable
    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public void setNegativeButtonText(@Nullable String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    @Nullable
    public Action getOnPositiveButtonClicked() {
        return onPositiveButtonClicked;
    }

    public void setOnPositiveButtonClicked(@Nullable Action onPositiveButtonClicked) {
        this.onPositiveButtonClicked = onPositiveButtonClicked;
    }

    @Nullable
    public Action getOnNegativeButtonClicked() {
        return onNegativeButtonClicked;
    }

    public void setOnNegativeButtonClicked(@Nullable Action onNegativeButtonClicked) {
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }
}
