package org.ergoplatform.mosaik.model.actions;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenInformationAction that = (TokenInformationAction) o;
        return Objects.equals(getTokenId(), that.getTokenId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTokenId());
    }
}
