/*

                Copyright (c) 2022 DevsOnFlutter (Devs On Flutter)
                            All rights reserved.

The plugin is governed by the BSD-3-clause License. Please see the LICENSE file
for more details.

*/

package com.devsonflutter.reflex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;

/* ReflexPlugin */
public class ReflexPlugin implements FlutterPlugin {

  /* ------------- Native Variables -------------- */
  @SuppressLint("StaticFieldLeak")
  public static Context context = null;

  private BinaryMessenger binaryMessenger = null;

  private MethodChannel methodChannel;
  private MethodCallHandler methodHandler;

  @SuppressLint("StaticFieldLeak")
  private static EventCallHandler eventHandler;

  private EventChannel eventChannel;
  /* ------------- Native Variables -------------- */

  /* ------------- Method Channel -------------- */
  private static final String CHANNEL_ID = "reflex_method_channel";

  public static String getChannelId() {
    return CHANNEL_ID;
  }
  /* ------------- Method Channel -------------- */

  /* ------------- Event Channel -------------- */
  private static final String STREAM_ID = "reflex_event_channel";

  public static String getStreamId() {
    return STREAM_ID;
  }
  /* ------------- Event Channel -------------- */

  /* ------------- Plugin Logging TAG -------------- */
  private static final String TAG = "[Reflex]";

  public static String getPluginTag() {
    return TAG;
  }
  /* ------------- Plugin Logging TAG -------------- */

  /* ------------- Flutter Variables -------------- */
  public static boolean debug;
  public static List<String> packageNameList = null;
  public static List<String> packageNameExceptionList = null;
  public static Map<String,Object> autoReply = null;
  public static boolean isAppInForeground = false;
  /* ------------- Flutter Variables -------------- */

  /* ------------- Utility Functions -------------- */
  static public void debugPrint(String message) {
    if(debug) {
      Log.d(TAG,message);
    }
  }
  /* ------------- Utility Functions -------------- */


  private void setupChannel(BinaryMessenger messenger, Context context) {
    methodChannel = new MethodChannel(binaryMessenger, CHANNEL_ID);
    eventChannel = new EventChannel(binaryMessenger,STREAM_ID);

    methodHandler = new MethodCallHandler();
    eventHandler = new EventCallHandler(context);

    methodChannel.setMethodCallHandler(methodHandler);
    eventChannel.setStreamHandler(eventHandler);
  }

  private void teardownChannel() {
    methodChannel.setMethodCallHandler(null);
    eventChannel.setStreamHandler(null);
    binaryMessenger = null;
    methodChannel = null;
    methodHandler = null;
    eventChannel = null;
    eventHandler = null;
    context = null;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    binaryMessenger = flutterPluginBinding.getBinaryMessenger();
    context = flutterPluginBinding.getApplicationContext();
    setupChannel(binaryMessenger, context);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (!isAppInForeground) {
      teardownChannel();
    }
  }
}
