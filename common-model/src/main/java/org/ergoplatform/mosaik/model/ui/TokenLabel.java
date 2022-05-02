package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;
import org.ergoplatform.mosaik.model.actions.TokenInformationAction;

import javax.annotation.Nullable;

/**
 * Label showing a token name and balance formatted. The label is clickable with a default
 * TokenInformationAction if no other action is set
 */
public class TokenLabel extends ViewElement {
    @Nullable private String tokenId;
    @Nullable private String tokenName;
    private int decimals;
    private long amount;

    @Nullable
    @Override
    public Action getOnClickAction() {
        Action onClickAction = super.getOnClickAction();

        if (onClickAction == null && tokenId != null) {
            return new TokenInformationAction(tokenId);
        } else  {
            return onClickAction;
        }
    }

    @Nullable
    public String getTokenId() {
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
