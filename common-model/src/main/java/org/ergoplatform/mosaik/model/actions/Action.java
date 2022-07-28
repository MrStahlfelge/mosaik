package org.ergoplatform.mosaik.model.actions;

import javax.annotation.Nonnull;

public interface Action {
    @Nonnull String getId();
    void setId(@Nonnull String id);
}
