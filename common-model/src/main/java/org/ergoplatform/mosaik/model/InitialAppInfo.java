package org.ergoplatform.mosaik.model;

import org.ergoplatform.mosaik.model.MosaikManifest;
import org.ergoplatform.mosaik.model.ViewContent;

import javax.annotation.Nonnull;

/**
 * On first request, the backend will respond with ViewContent completed by a manifest
 */
public class InitialAppInfo extends ViewContent {
    private MosaikManifest manifest;

    @Nonnull
    public MosaikManifest getManifest() {
        if (manifest == null) {
            throw new IllegalStateException("manifest must not be null");
        }

        return manifest;
    }

    public void setManifest(MosaikManifest manifest) {
        this.manifest = manifest;
    }
}
