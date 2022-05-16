package org.ergoplatform.mosaik.model;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * With a MosaikContext a Wallet application executing a Mosaik app can declare properties the
 * app is executed with.
 */
public class MosaikContext {
    public final int mosaikVersion;

    /**
     * Like a “cookie”, the GUID helps to identify the user to store preferences for the user on
     * server-side. The GUID does not change (unless user explicitly triggers this) and will be the
     * same for all apps belonging to the same domain
     */
    @Nonnull
    public final String guid;

    /**
     * Preferred user language
     */
    @Nonnull
    public final String language;

    /**
     * Name of the executing wallet application. The Mosaik app can use this to switch behaviour
     * for different Mosaik executors. This should not be necessary, but we all know the truth.
     */
    @Nonnull
    public final String walletAppName;

    /**
     * @see #walletAppName
     */
    @Nonnull
    public final String walletAppVersion;

    @Nonnull
    public final Platform walletAppPlatform;

    public MosaikContext(int mosaikVersion, @Nonnull String guid, @Nonnull String language,
                         @Nonnull String walletAppName, @Nonnull String walletAppVersion,
                         @Nonnull Platform walletAppPlatform) {
        this.mosaikVersion = mosaikVersion;
        this.walletAppVersion = walletAppVersion;
        this.walletAppPlatform = walletAppPlatform;
        Objects.requireNonNull(guid);
        Objects.requireNonNull(walletAppName);
        Objects.requireNonNull(walletAppVersion);
        Objects.requireNonNull(walletAppPlatform);

        this.guid = guid;
        this.language = language;
        this.walletAppName = walletAppName;
    }

    public enum Platform {
        DESKTOP,
        TABLET,
        PHONE
    }
}
