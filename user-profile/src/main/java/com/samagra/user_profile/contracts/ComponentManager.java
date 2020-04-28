package com.samagra.user_profile.contracts;

import com.samagra.commons.MainApplication;
import com.samagra.user_profile.ProfileSectionDriver;

public class ComponentManager {
    public static IProfileContract iProfileContract;

    /**
     *
     * @param profileContractImpl
     * @param application
     * @param baseURL
     * @param applicationID
     */
    public static void registerProfilePackage(IProfileContract profileContractImpl, MainApplication application,
                                              String baseURL, String applicationID, String sendOTPUrl,
                                              String updatePasswordUrl, String fusionAuthKey, String userID) {
        ProfileSectionDriver.init(application, baseURL, applicationID,  sendOTPUrl, updatePasswordUrl, fusionAuthKey, userID);
        iProfileContract = profileContractImpl;
    }

}