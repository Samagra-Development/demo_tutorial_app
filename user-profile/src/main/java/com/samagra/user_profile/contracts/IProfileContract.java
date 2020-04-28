package com.samagra.user_profile.contracts;

import android.content.Context;

import androidx.annotation.NonNull;

import com.samagra.user_profile.profile.UserProfileElement;

import java.util.ArrayList;

public interface IProfileContract {
    /**
     *  @param context
     * @param userProfileElements
     * @param userProfileElements
     * @param fusionAuthApiKey
     */
    void launchProfileActivity(@NonNull Context context, ArrayList<UserProfileElement> userProfileElements,
                               String fusionAuthApiKey);

}