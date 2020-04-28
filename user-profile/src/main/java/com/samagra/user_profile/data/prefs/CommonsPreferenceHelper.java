package com.samagra.user_profile.data.prefs;

import com.samagra.user_profile.data.network.model.LoginResponse;

/**
 * Interface defining the access point to the SharedPreference used by the ancillaryscreens module.
 * All access functions to be implemented by a single solid implementation of this interface.
 *
 * @author Pranav Sharma
 * @see CommonsPrefsHelperImpl
 */
public interface CommonsPreferenceHelper {
    String getCurrentUserName();

    String getCurrentUserId();


    void updateProfileKeyValuePair(String key, String value);

    String getProfileContentValueForKey(String key);
}
