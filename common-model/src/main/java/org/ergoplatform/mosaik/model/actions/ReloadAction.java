package org.ergoplatform.mosaik.model.actions;

import org.ergoplatform.mosaik.model.Since;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Reloads the app when called
 */
@Since(0)
public class ReloadAction implements Action {
    private String id;

    @Nonnull
    @Override
    public String getId() {
        if (id == null) {
            throw new IllegalStateException("Action id must not be null");
        }

        return id;
    }

    @Override
    public void setId(@Nonnull String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReloadAction that = (ReloadAction) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
