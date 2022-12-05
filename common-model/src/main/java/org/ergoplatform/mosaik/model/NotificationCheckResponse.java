package org.ergoplatform.mosaik.model;

import javax.annotation.Nullable;

public class NotificationCheckResponse {

    /**
     * Notification to show to the user. Subsequent calls will replace a former results,
     * so returning null here will remove former notifications stored for the user
     */
    @Nullable
    public String message;

    /**
     * Defines in how many minutes a next check should be done. It is not guaranteed that a
     * Mosaik executor will perform the check at that time.
     */
    public int nextCheck;
}
