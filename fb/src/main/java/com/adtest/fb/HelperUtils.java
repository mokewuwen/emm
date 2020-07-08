package com.adtest.fb;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

public class HelperUtils {
    public static void init(Context applicationContext) {
        FacebookSdk.sdkInitialize(applicationContext);
        AudienceNetworkAds.initialize(applicationContext);
        AdSettings.setDebugBuild(BuildConfig.DEBUG);
    }
}
