package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;
import org.ergoplatform.mosaik.model.ui.ForegroundColor;
import org.ergoplatform.mosaik.model.ui.ViewElement;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Label showing a token name and balance formatted. The label is clickable with a default
 * TokenInformationAction if no other action is set
 *
 * When decorated is true, applications might show preview pictures, logos, verification signs etc.
 */
@Since(1)
public class TokenLabel extends ViewElement implements StyleableLabel {
    @Nullable
    private String tokenId;
    @Nullable
    private String tokenName; // fallback
    private int decimals; // fallback
    @Nullable
    private Long amount;
    private boolean decorated = true;
    @Nonnull
    private LabelStyle style = LabelStyle.BODY1;
    @Nonnull
    private ForegroundColor textColor = ForegroundColor.DEFAULT;

    @Nonnull
    public String getTokenId() {
        if (tokenId == null) {
            throw new IllegalStateException("No tokenId provided for " + this.getClass().getSimpleName());
        }

        return tokenId;
    }

    public void setTokenId(@Nullable String tokenId) {
        this.tokenId = tokenId;
    }

    @Nullable
    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(@Nullable String tokenName) {
        this.tokenName = tokenName;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    @Nullable
    public Long getAmount() {
        return amount;
    }

    public void setAmount(@Nullable Long amount) {
        this.amount = amount;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }

    @Override
    @Nonnull
    public LabelStyle getStyle() {
        return style;
    }

    @Override
    public void setStyle(@Nonnull LabelStyle style) {
        this.style = style;
    }

    @Override
    @Nonnull
    public ForegroundColor getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(@Nonnull ForegroundColor textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TokenLabel that = (TokenLabel) o;
        return getDecimals() == that.getDecimals() && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getTokenId(), that.getTokenId()) && Objects.equals(getTokenName(), that.getTokenName()) && isDecorated() == that.isDecorated() && getStyle() == that.getStyle() && getTextColor() == that.getTextColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStyle(), getTextColor(), getTokenId(), getTokenName(), getDecimals(), getAmount(), isDecorated());
    }
}
