package com.samagra.ancillaryscreens.data.network;

import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.commons.MainApplication;

/**
 * Class that contains all the API endpoints required by the Ancillary Screens.
 * Note : This class should only contain static final String variables indicating the API endpoints. {@link AncillaryScreensDriver#BASE_API_URL}
 * is provided in {@link AncillaryScreensDriver#init(MainApplication, String, String, String)}
 *
 * @author Pranav Sharma
 * @see AncillaryScreensDriver#init(MainApplication, String, String, String)
 */
final class BackendApiUrls {
    static final String AUTH_LOGIN_ENDPOINT = AncillaryScreensDriver.BASE_API_URL + "/api/login";
    static final String USER_DETAILS_ENDPOINT = AncillaryScreensDriver.BASE_API_URL + "/api/user/{user_id}/";
    static final String USER_SEARCH_ENDPOINT = AncillaryScreensDriver.BASE_API_URL + "/api/user/search";
}
