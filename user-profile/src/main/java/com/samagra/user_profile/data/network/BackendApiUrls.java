package com.samagra.user_profile.data.network;

import com.samagra.user_profile.ProfileSectionDriver;

/**
 * Class that contains all the API endpoints required by the Ancillary Screens.
 *
 */


///Note : This class should only contain static final String variables indicating the API endpoints. {@link AncillaryScreensDriver#BASE_API_URL}
// * is provided in {@link AncillaryScreensDriver#init(MainApplication, String)}
// *
// * @author Pranav Sharma
// * @see AncillaryScreensDriver#init(MainApplication, String)
final class BackendApiUrls {
    static final String USER_DETAILS_ENDPOINT = ProfileSectionDriver.BASE_API_URL +  "/api/user/{user_id}/";
    static final String USER_SEARCH_ENDPOINT =  ProfileSectionDriver.BASE_API_URL+ "/api/user/search";
}
