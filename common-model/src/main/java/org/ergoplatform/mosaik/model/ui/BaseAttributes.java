package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import javax.annotation.Nullable;

public interface BaseAttributes {
    boolean isVisible();

    void setVisible(boolean visible);

    @Nullable
    String getId();

    @Nullable
    Action getOnLongPressAction();

    void setOnLongPressAction(@Nullable Action action);

    @Nullable
    Action getOnClickAction();

    void setOnClickAction(@Nullable Action action);
}
