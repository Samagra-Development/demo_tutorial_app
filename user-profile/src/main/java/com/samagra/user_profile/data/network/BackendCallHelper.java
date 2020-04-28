package com.samagra.user_profile.data.network;

import com.samagra.user_profile.data.network.model.LoginRequest;
import com.samagra.user_profile.data.network.model.LoginResponse;

import org.json.JSONObject;

import io.reactivex.Single;

/**
 * Interface containing all the API Calls performed by this module.
 * All calls to be implemented in a single implementation of this interface.
 *
 * @author Pranav Sharma
 * @see BackendCallHelperImpl
 */
public interface BackendCallHelper {

    Single<JSONObject> performGetUserDetailsApiCall(String userId, String apiKey);

    Single<JSONObject> performPutUserDetailsApiCall(String userId, String apiKey, JSONObject jsonObject);

    Single<JSONObject> performSearchUserByPhoneCall(String phone, String apiKey);

    Single<JSONObject> performSearchUserByEmailCall(String phone, String apiKey);
}
