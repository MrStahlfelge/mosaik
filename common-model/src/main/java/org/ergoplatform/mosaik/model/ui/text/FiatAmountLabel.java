package org.ergoplatform.mosaik.model.ui.text;

/**
 * Shows nanoERG amount in user's currency, formatted
 */
public class FiatAmountLabel extends StyleableTextLabel<Long> {
    private boolean fallbackToErg = false;

    /**
     * @return true if label should show ERG amount when no fiat currency is sets
     */
    public boolean isFallbackToErg() {
        return fallbackToErg;
    }

    public void setFallbackToErg(boolean fallbackToErg) {
        this.fallbackToErg = fallbackToErg;
    }
}
