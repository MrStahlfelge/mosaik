package org.ergoplatform.mosaik.model.ui;

import org.ergoplatform.mosaik.model.actions.Action;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Button type element to let the user choose one of his wallets.
 * <p>
 * {@link #getValue()} might contain a list of wallet addresses or a single address.
 */
public class WalletChooseButton extends ViewElement implements InputElement<List<String>> {
    @Nullable
    private List<String> addresses;
    @Nullable
    private Action onValueChangedAction;
    private boolean enabled;

    @Nullable
    @Override
    public List<String> getValue() {
        return addresses;
    }

    @Override
    public void setValue(@Nullable List<String> value) {
        addresses = value;
    }

    @Nullable
    @Override
    public Action getOnValueChangedAction() {
        return onValueChangedAction;
    }

    @Override
    public void setOnValueChangedAction(@Nullable Action action) {
        onValueChangedAction = action;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setOnClickAction(@Nullable Action action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }
}
