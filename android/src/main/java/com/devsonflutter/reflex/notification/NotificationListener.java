/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;

import com.devsonflutter.reflex.ReflexPlugin;
import com.devsonflutter.reflex.notification.autoReply.AutoReply;

import java.util.Map;

/* Notification Listener */
@RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = ReflexPlugin.getPluginTag();

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        // Ignore group summary notification
        if ((notification.getNotification().flags & Notification.FLAG_GROUP_SUMMARY) != 0) {
            return;
        }

        // Package name as title
        String packageName = notification.getPackageName();

        // Extra Payload
        Bundle extras = notification.getNotification().extras;

        Intent intent = new Intent(NotificationUtils.NOTIFICATION_INTENT);
        intent.putExtra(NotificationUtils.NOTIFICATION_PACKAGE_NAME, packageName);

        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

        if(title==null) {
           title = "Untitled Notification";
        }

        if(text == null){
            text = "No message!";
        }
        
        int notificationId = notification.getId(); // Get the notification ID
        intent.putExtra(NotificationUtils.NOTIFICATION_ID, notificationId);
        intent.putExtra(NotificationUtils.NOTIFICATION_TITLE, title.toString());
        intent.putExtra(NotificationUtils.NOTIFICATION_MESSAGE, text.toString());
        
        addActiveNotification(notification);

        // Notification Receiver listens to this broadcast
        sendBroadcast(intent);

        // Sending AutoReply
        if(NotificationUtils.canReply(notification))
        {
            final Map<String, Object> autoReply = ReflexPlugin.autoReply;
            String message = (String) autoReply.get("message");
            // Reply to notification
            new AutoReply(ReflexPlugin.context).sendReply(notification, packageName, title, message);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void addActiveNotification(StatusBarNotification notification) {
        if (!NotificationUtils.activeNotifications.contains(notification)) {
            NotificationUtils.activeNotifications.add(notification);
        }
    }

}

