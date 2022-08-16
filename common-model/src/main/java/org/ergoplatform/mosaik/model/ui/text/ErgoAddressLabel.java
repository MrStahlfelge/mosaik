package org.ergoplatform.mosaik.model.ui.text;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Label showing an address, providing share/copy and address book functionality
 * (in case application supports it).
 */
@Since(1)
public class ErgoAddressLabel extends StyleableTextLabel<String>implements ExpandableElement {
    private boolean expandOnClick = true;

    @Override
    public boolean isExpandOnClick() {
        return expandOnClick;
    }

    @Override
    public void setExpandOnClick(boolean expandOnClick) {
        this.expandOnClick = expandOnClick;
    }

    @Override
    public void setOnClickAction(@Nullable String action) {
        throw new IllegalArgumentException("OnClickAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    public void setOnLongPressAction(@Nullable String action) {
        throw new IllegalArgumentException("OnLongPressAction can't be set for" +
                this.getClass().getSimpleName());
    }

    @Nonnull
    @Override
    public TruncationType getTruncationType() {
        return TruncationType.MIDDLE;
    }

    @Override
    public void setTruncationType(@Nonnull TruncationType truncationType) {
        throw new IllegalArgumentException("truncationType can't be set for" +
                this.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ErgoAddressLabel that = (ErgoAddressLabel) o;
        return isExpandOnClick() == that.isExpandOnClick();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isExpandOnClick());
    }
}
