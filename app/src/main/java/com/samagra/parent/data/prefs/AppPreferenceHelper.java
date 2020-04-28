package com.samagra.parent.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.samagra.commons.LocaleManager;
import com.samagra.parent.di.ApplicationContext;
import com.samagra.parent.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Solid implementation of the {@link PreferenceHelper}, performs the read/write operations on the {@link SharedPreferences}
 * used by the app module. The class is injected to all the activities instead of manually creating an object.
 *
 * @author Pranav Sharma
 */
@Singleton
public class AppPreferenceHelper implements PreferenceHelper {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences defaultPreferences;

    @Inject
    public AppPreferenceHelper(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        this.sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getCurrentUserName() {
        return defaultPreferences.getString("user.fullName", "");
    }

    @Override
    public void updateFormVersion(String version) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("formVersion", version);
        editor.apply();
    }

    @Override
    public String getFormVersion() {
        return sharedPreferences.getString("formVersion", "0");
    }

    @Override
    public String fetchCurrentSystemLanguage() {
        if(defaultPreferences.getString("currentLanguage", "").isEmpty()) {
            defaultPreferences.edit().putString("currentLanguage", LocaleManager.HINDI).apply();
            return LocaleManager.HINDI;
        }else{
           return defaultPreferences.getString("currentLanguage", "");
        }
    }

}
