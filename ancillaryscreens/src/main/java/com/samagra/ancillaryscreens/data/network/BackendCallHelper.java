package com.samagra.ancillaryscreens.data.network;

import com.samagra.ancillaryscreens.data.network.model.LoginRequest;
import com.samagra.ancillaryscreens.data.network.model.LoginResponse;

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

    Single<LoginResponse> performLoginApiCall(LoginRequest loginRequest);

    Single<JSONObject> performGetUserDetailsApiCall(String userId, String apiKey);

    Single<JSONObject> performPutUserDetailsApiCall(String userId, String apiKey, JSONObject jsonObject);

    Single<JSONObject> performSearchUserByPhoneCall(String phone, String apiKey, String applicationID);

    Single<JSONObject> performSearchUserByEmailCall(String phone, String apiKey, String applicationID);
}
