package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

/**
 * Will open detailed information about a token
 */
public class TokenInformationAction extends Action {
    @Nonnull private final String tokenId;

    public TokenInformationAction(@Nonnull String tokenId) {
        this.tokenId = tokenId;
    }
}
