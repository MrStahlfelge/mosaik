package org.ergoplatform.mosaik.model.ui;

import java.util.List;

import javax.annotation.Nonnull;

public interface ViewGroup {
    @Nonnull
    List<ViewElement> getChildren();

    void addChildren(@Nonnull ViewElement element);
}
