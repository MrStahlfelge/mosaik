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
     * Timestamp of the message. This can be used to determine if the message is new and should
     * be marked unread.
     */
    public long messageTs;

    /**
     * Defines in how many minutes a next check should be done (delta time interval). It is not
     * guaranteed that a Mosaik executor will perform the check at that time.
     */
    public int nextCheck;
}
