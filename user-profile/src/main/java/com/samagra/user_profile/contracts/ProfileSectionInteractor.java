package com.samagra.user_profile.contracts;

import android.content.Context;

import androidx.annotation.NonNull;

import com.samagra.user_profile.ProfileSectionDriver;
import com.samagra.user_profile.profile.UserProfileElement;

import java.util.ArrayList;

public class ProfileSectionInteractor implements IProfileContract {

    private ProfileUpdateListener profileUpdateListener;

    @Override
    public void launchProfileActivity(@NonNull Context context, ArrayList<UserProfileElement> userProfileElements,
                                      String fusionAuthApiKey){
        ProfileSectionDriver.launchProfileActivity(context, userProfileElements, fusionAuthApiKey);
    }


}