package com.androidbroadcastreceiverforreferrer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Set;

/**
 * Created by shivam.aditya on 08-01-2018.
 */

public class ReferrerBroadcastReceiver extends BroadcastReceiver {
    public static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    private static void savePreference(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("googleReferrerSharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Integer) {
            Integer intValue = (Integer) value;
            if (intValue != -1) {
                editor.putInt(key, intValue);
            }
        } else if (value instanceof Long) {
            Long longValue = (Long) value;
            if (longValue != -1) {
                editor.putLong(key, longValue);
            }
        } else if (value instanceof Boolean) {
            Boolean booleanValue = (Boolean) value;
            if (booleanValue != null) {
                editor.putBoolean(key, booleanValue);
            }
        } else if (value instanceof Set) {
            Set<String> stringSet = (Set<String>) value;
            editor.putStringSet(key, stringSet);
        } else if (value instanceof Float) {
            Float floatValue = (Float) value;
            if (Float.compare(floatValue, -1.0f) != 0) {
                editor.putFloat(key, floatValue);
            }
        }
        editor.apply();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!INSTALL_ACTION.equals(intent.getAction())) {
            return;
        }

        String referrer = intent.getStringExtra("referrer");
        Log.d("AppInstall", referrer);
        Log.d("ReactNativeJS", "ReferrerBroadcastReceiver referrer: " + referrer);

        // Will require for sending other events
        // savePreference(context,"GoogleInstallReferrer",referrer);

        //save the intent uri in shared preferences
        //PreferenceManager.saveString(Constants.REFERRER_RECEIVER_INTENT_URI, intent.toUri(Intent.URI_INTENT_SCHEME));

        //OnAppRegistrationController.getInstance().sendGoogleReferrerDetails();
        //AppsFlyerHelper.extractDeepLinkFromReferrer(referrer);

        WritableMap map = new WritableNativeMap();
        map.putString("referrer", referrer);
        sendEvent((ReactContext)context,"ReferrerBroadcastReceiver" ,map);
        //MSREventBridgeModule.emitEvent("ReferrerBroadcastReceiver", map);
    }

    private void sendEvent(ReactContext reactContext, String eventName, WritableMap map) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, map);
    }
}