package com.samagra.user_profile.profile;

import com.samagra.user_profile.base.BaseInteractor;
import com.samagra.user_profile.data.prefs.CommonsPreferenceHelper;

import javax.inject.Inject;

public class ProfileInteractor extends BaseInteractor implements ProfileContract.Interactor {

    @Inject
    public ProfileInteractor(CommonsPreferenceHelper preferenceHelper) {
        super(preferenceHelper);
    }

    /**
     * This methods updates a single profile property which is represented in the
     * {@link android.content.SharedPreferences} by key with a given value.
     *
     * @param key   - The key with which the property is represented in the {@link android.content.SharedPreferences}
     * @param value - The value that needs to be stored against the key in the {@link android.content.SharedPreferences}
     */
    @Override
    public void updateContentKeyInSharedPrefs(String key, String value) {
        getPreferenceHelper().updateProfileKeyValuePair(key, value);
    }

    @Override
    public String getActualContentValue(String key) {
        return getPreferenceHelper().getProfileContentValueForKey(key);
    }

    @Override
    public String getCurrentUserId() {
        return getPreferenceHelper().getCurrentUserId();
    }
}
