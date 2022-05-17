package org.ergoplatform.mosaik.model;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Manifest for a Mosaik app. A Mosaik app can declare properties to the Wallet application that
 * is executing it.
 */
public class MosaikManifest {
    /**
     * name to be shown for this Mosaik App
     */
    @Nonnull
    public String appName;

    /**
     * icon to be shown for this Mosaik app
     */
    @Nullable
    public String iconUrl;

    /**
     * defines the current apps version. If the version flag changes while a user runs the app,
     * it will be reloaded from base URL.
     */
    public final int appVersion;

    /**
     * the Mosaik version this app was tested with and developed for. Newer Mosaik versions
     * should apply backwards compatibility, if possible.
     */
    public final int targetMosaikVersion;

    /**
     * declares the canvas size this app is intended to be drawn on. If an app targeting a platform
     * is run on a platform with more screen size , it will be shown within a centered box of around
     * the screen size of its target platform.
     */
    @Nullable
    public final Canvas targetCanvas;

    /**
     * Mosaik version this app needs at mininum. Older Mosaik versions should not run this app.
     */
    public final int minMosaikVersion;

    /**
     * Cache life time (in seconds) determines how long a viewtree is valid after it was loaded.
     * Users can leave an application or forget it in background. If the cache lifetime expires,
     * the view tree will be reloaded from the apps base URL.
     * Min 30 seconds. Set 0 for infinite cache life time. The executing application might
     * have own limitations.
     */
    public final int cacheLifetime;

    /**
     * Any errors logged while running the Mosaik app will be logged to this address (POST-Request)
     */
    @Nullable
    public String errorReportUrl;

    public MosaikManifest(@Nonnull String appName, @Nullable String iconUrl,
                          int appVersion, int targetMosaikVersion,
                          @Nullable Canvas targetCanvas, int minMosaikVersion,
                          int cacheLifetime, @Nullable String errorReportUrl) {
        this.targetCanvas = targetCanvas;
        Objects.requireNonNull(appName);

        this.appName = appName;
        this.iconUrl = iconUrl;
        this.appVersion = appVersion;
        this.cacheLifetime = cacheLifetime;
        this.targetMosaikVersion = targetMosaikVersion;
        this.minMosaikVersion = minMosaikVersion;
        this.errorReportUrl = errorReportUrl;
    }

    public enum Canvas {
        /**
         * Portrait canvas comparable to a mobile phone or a browser side bar
         */
        PORTRAIT_SMALL,
        /**
         * Portrait canvas comparable to a tablet in portrait mode
         */
        PORTRAIT_MEDIUM,
        /**
         * Full screen canvas in landscape mode
         */
        LANDSCAPE
    }
}
