package org.ergoplatform.mosaik.model.ui;

import javax.annotation.Nullable;

public class RootView {
    private ViewElement content;
    private long cacheLifeTime;

    @Nullable
    public ViewElement getContent() {
        return content;
    }

    public void setContent(@Nullable ViewElement content) {
        this.content = content;
    }

    public long getCacheLifeTime() {
        return cacheLifeTime;
    }

    public void setCacheLifeTime(long cacheLifeTime) {
        this.cacheLifeTime = cacheLifeTime;
    }
}
