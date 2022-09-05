package org.ergoplatform.mosaik.model;

import org.ergoplatform.mosaik.model.actions.BackendRequestAction;

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
    public final String appName;

    /**
     * Description text for this Mosaik app
     */
    @Nullable
    public String appDescription;

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
    public final CanvasDimension targetCanvasDimension;

    /**
     * Cache life time (in seconds) determines how long a viewtree is valid after it was loaded
     * or altered.
     * Users can leave an application or forget it in background. If the cache lifetime expires,
     * the view tree will be reloaded from the apps base URL.
     * Min 30 seconds. Set 0 for infinite cache life time. The executing application might
     * have own limitations.
     */
    public final int cacheLifetime;

    /**
     * Action ID to call when the app is loaded for the first time or reloaded. Can be used to
     * automatically launch a {@link org.ergoplatform.mosaik.model.actions.ChangeSiteAction}
     * setting up the view.
     * Note that other action types are not allowed to be used.
     */
    @Nullable
    @Since(2)
    public String onAppLoadedAction;

    /**
     * Action ID to call when a resize of the application leads to a change of the
     * {@link MosaikContext#walletAppPlatform} value.
     * Useable action types are ReloadAction, ChangeSiteAction, BackendRequestAction
     */
    @Nullable
    @Since(2)
    public String onResizeAction;

    /**
     * Any errors logged while running the Mosaik app will be logged to this address (POST-Request)
     */
    @Nullable
    public String errorReportUrl;

    public MosaikManifest(@Nonnull String appName,
                          int appVersion, int targetMosaikVersion,
                          @Nullable CanvasDimension targetCanvasDimension,
                          int cacheLifetime) {
        this.targetCanvasDimension = targetCanvasDimension;
        Objects.requireNonNull(appName);

        this.appName = appName;
        this.appVersion = appVersion;
        this.cacheLifetime = cacheLifetime;
        this.targetMosaikVersion = targetMosaikVersion;
    }

    public enum CanvasDimension {
        /**
         * Portrait canvas comparable to a mobile phone or a browser side bar
         */
        COMPACT_WIDTH,
        /**
         * Portrait canvas comparable to a tablet in portrait mode
         */
        MEDIUM_WIDTH,
        /**
         * Full screen canvas in landscape mode
         */
        FULL_WIDTH
    }
}
