package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shows an error or info dialog that is shown modal on top of the current view.
 */
@Since(0)
public class DialogAction implements Action {
    String message;
    @Nullable String positiveButtonText;
    @Nullable String negativeButtonText;
    @Nullable String onPositiveButtonClicked;
    @Nullable String onNegativeButtonClicked;
    private String id;

    @Nonnull
    @Override
    public String getId() {
        if (id == null) {
            throw new IllegalStateException("Action id must not be null");
        }

        return id;
    }

    public void setId(@Nonnull String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    @Nonnull
    public String getMessage() {
        if (message == null) {
            throw new IllegalStateException("No message text set for " + this.getClass().getSimpleName());
        }

        return message;
    }

    public void setMessage(@Nonnull String message) {
        Objects.requireNonNull(message);
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
    public String getOnPositiveButtonClicked() {
        return onPositiveButtonClicked;
    }

    public void setOnPositiveButtonClicked(@Nullable String onPositiveButtonClicked) {
        this.onPositiveButtonClicked = onPositiveButtonClicked;
    }

    @Nullable
    public String getOnNegativeButtonClicked() {
        return onNegativeButtonClicked;
    }

    public void setOnNegativeButtonClicked(@Nullable String onNegativeButtonClicked) {
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DialogAction that = (DialogAction) o;
        return Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getPositiveButtonText(), that.getPositiveButtonText()) && Objects.equals(getNegativeButtonText(), that.getNegativeButtonText()) && Objects.equals(getOnPositiveButtonClicked(), that.getOnPositiveButtonClicked()) && Objects.equals(getOnNegativeButtonClicked(), that.getOnNegativeButtonClicked());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getPositiveButtonText(), getNegativeButtonText(), getOnPositiveButtonClicked(), getOnNegativeButtonClicked());
    }
}
