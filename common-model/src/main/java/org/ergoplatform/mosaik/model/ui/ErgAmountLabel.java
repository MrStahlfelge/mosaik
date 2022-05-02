package org.ergoplatform.mosaik.model.ui;

/**
 * Shows nanoERG amount formatted
 */
public class ErgAmountLabel extends StyleableTextLabel<Long> {
    private boolean withCurrencySymbol = false;
    private int maxDecimals = 4;
    private boolean trimTrailingZero = false;

    /**
     * @return true if currency symbol ("ERG") should be shown
     */
    public boolean isWithCurrencySymbol() {
        return withCurrencySymbol;
    }

    public void setWithCurrencySymbol(boolean withCurrencySymbol) {
        this.withCurrencySymbol = withCurrencySymbol;
    }

    /**
     * @return max number of decimals to use
     */
    public int getMaxDecimals() {
        return maxDecimals;
    }

    public void setMaxDecimals(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }

    /**
     * @return true if trailing zeroes should be omitted.
     */
    public boolean isTrimTrailingZero() {
        return trimTrailingZero;
    }

    public void setTrimTrailingZero(boolean trimTrailingZero) {
        this.trimTrailingZero = trimTrailingZero;
    }
}
