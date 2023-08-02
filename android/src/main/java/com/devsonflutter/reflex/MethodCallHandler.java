/*

               Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.devsonflutter.reflex.notification.autoReply.AutoReply;
import com.devsonflutter.reflex.permission.NotificationPermission;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandler implements MethodChannel.MethodCallHandler {

    private static final String TAG = ReflexPlugin.getPluginTag();
    private static final boolean debug = ReflexPlugin.debug;

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        NotificationPermission notificationPermission = new NotificationPermission(ReflexPlugin.context);

        switch (call.method) {
            case "isPermissionGranted":
                boolean isPermissionGranted = notificationPermission.permissionGranted();
                result.success(isPermissionGranted);
                break;
            case "requestPermission":
                notificationPermission.requestPermission();
                break;
            case "sendReply":
                break;
            case "replyToNotification":
                
                // get notification id from flutter
                int notificationId = call.argument("notificationId");
                // get reply from flutter
                String reply = call.argument("reply");
               
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new AutoReply(ReflexPlugin.context).sendReply(notificationId, reply);
                }
                break;
            case "replyToNotificationByTitle":
                // get notification title from flutter
                String notificationTitle = call.argument("title");
                // get reply from flutter
                String replyText = call.argument("reply");
               
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new AutoReply(ReflexPlugin.context).sendReply(notificationTitle, replyText);
                }
                break;
            default:
                result.notImplemented();
                break;
        }

    }
}
