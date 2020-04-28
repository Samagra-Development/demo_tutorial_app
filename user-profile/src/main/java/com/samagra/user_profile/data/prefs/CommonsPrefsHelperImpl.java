package com.samagra.user_profile.data.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.samagra.user_profile.GeneralKeys;
import com.samagra.user_profile.data.network.model.LoginResponse;
import com.samagra.user_profile.di.ApplicationContext;
import com.samagra.user_profile.di.PreferenceInfo;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Solid implementation of {@link CommonsPreferenceHelper}, performs the read/write operations on the {@link SharedPreferences}
 * used by the ancillaryscreens. The class is injected to all activities instead of manually creating an object.
 *
 * @author Pranav Sharma
 */
public class CommonsPrefsHelperImpl implements CommonsPreferenceHelper {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences defaultPreferences;
    Context context;

    @Inject
    public CommonsPrefsHelperImpl(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        this.sharedPreferences = context.getSharedPreferences(prefFileName, MODE_PRIVATE);
        this.context = context;
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getCurrentUserName() {
        return defaultPreferences.getString("user.fullName", "");
    }

    @Override
    public String getCurrentUserId() {
        return defaultPreferences.getString("user.id", "");
    }


    @Override
    public void updateProfileKeyValuePair(String key, String value) {
        SharedPreferences.Editor editor = defaultPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public String getProfileContentValueForKey(String key) {
        return defaultPreferences.getString(key, "");
    }

}
