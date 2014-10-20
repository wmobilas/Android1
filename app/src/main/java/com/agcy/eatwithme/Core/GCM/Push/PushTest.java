package com.agcy.eatwithme.Core.GCM.Push;

import android.app.PendingIntent;

/**
 * Created by kiolt_000 on 09/09/2014.
 */
public class PushTest extends Push {
    private final String title;
    private final String message;

    public PushTest(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    protected int getNotificationId() {
        return TEST_NOTIFICATION;
    }

    @Override
    protected PendingIntent getPendingIntent() {
        return null;
    }
}
