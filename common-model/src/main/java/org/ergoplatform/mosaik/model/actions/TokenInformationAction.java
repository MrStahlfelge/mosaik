package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

/**
 * Will open detailed information about a token
 */
public class TokenInformationAction implements Action {
    private String tokenId;

    public TokenInformationAction(String tokenId) {
        this.tokenId = tokenId;
    }

    @Nonnull
    public String getTokenId() {
        if (tokenId == null) {
            throw new IllegalStateException("No token id set for " + this.getClass().getSimpleName());
        }

        return tokenId;
    }

    public void setTokenId(@Nonnull String tokenId) {
        this.tokenId = tokenId;
    }
}
