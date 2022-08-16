package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public interface BaseAttributes {
    boolean isVisible();

    void setVisible(boolean visible);

    @Nullable
    String getId();

    @Nullable
    String getOnLongPressAction();

    void setOnLongPressAction(@Nullable String action);

    @Nullable
    String getOnClickAction();

    void setOnClickAction(@Nullable String action);
}
