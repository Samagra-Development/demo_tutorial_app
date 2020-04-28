package com.samagra.user_profile.profile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.samagra.user_profile.ProfileSectionDriver;
import com.samagra.user_profile.base.BasePresenter;
import com.samagra.user_profile.data.network.BackendCallHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;


public class ProfilePresenter<V extends ProfileContract.View, I extends ProfileContract.Interactor> extends BasePresenter<V, I> implements ProfileContract.Presenter<V, I> {
    /**
     * These dependencies are provided by the {@link com.samagra.user_profile.di.modules.CommonsActivityModule} and
     * {@link com.samagra.user_profile.di.modules.CommonsActivityAbstractProviders} and are required by the Presenter
     * to carry out business logic tasks related to database access and/or netwrok access.
     *
     * @param mvpInteractor       - The interactor for the Activity. Must implement {@link com.samagra.user_profile.base.MvpInteractor}
     * @param apiHelper           - It is the {@link com.samagra.user_profile.data.network.BackendCallHelperImpl} singleton instance
     *                            required for performing N/W calls.
     * @param compositeDisposable - A {@link CompositeDisposable} object that keeps a track of all the API calls being
     *                            made in the current activity context. This object allows you to dispose all the calls
     */
    @Inject
    public ProfilePresenter(I mvpInteractor, BackendCallHelper apiHelper, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, apiHelper, compositeDisposable);
    }

    /**
     * Initiates the SendOtpTask that sends an OTP on the user phone number.
     *
     * @param userPhone - The mobile number of the user on which the OTP needs to be send.
     */
    @Override
    public void startSendOTPTask(@NonNull String userPhone) {

    }

    /**
     * Updates the User's profile properties in {@link android.content.SharedPreferences}. The
     * updated properties are provided through the profileElementHolders parameter. This function
     * uses the {@link ProfileContract.Interactor} to access the {@link android.content.SharedPreferences}
     *
     * @param profileElementHolders - A list {@link ProfileElementViewHolders.ProfileElementHolder}s through which updated
     *                              values of a user profile can be accessed.
     */
    @Override
    public void updateUserProfileLocally(ArrayList<ProfileElementViewHolders.ProfileElementHolder> profileElementHolders) {
        UserProfileElement userProfileElement;
        for (ProfileElementViewHolders.ProfileElementHolder elementHolder : profileElementHolders) {
            userProfileElement = elementHolder.getUserProfileElement();
            String contentKey = userProfileElement.getContent();
            String updatedValue = elementHolder.getUpdatedElementValue();
            ProfileSectionDriver.sendEvent(contentKey, updatedValue);
            getMvpInteractor().updateContentKeyInSharedPrefs(contentKey, updatedValue);
        }
    }

    public boolean testIfPhoneUnique(String phone, String userId){
        boolean isPhoneUnique = true;
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject bodyObject = new JSONObject();
        JSONObject search = new JSONObject();
        try {
            JSONArray sortFields = new JSONArray();
            JSONObject sortFieldsBody = new JSONObject();
            sortFieldsBody.put("name", "email");
            sortFields.put(sortFieldsBody);
            search.put("queryString", "(data.phone: " + phone + ") AND (registrations.applicationId: " + ProfileSectionDriver.applicationID +")");
            search.put("sortFields", sortFields);
            bodyObject.put("search", search);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, bodyObject.toString());
        Request request = new Request.Builder()
                .url(ProfileSectionDriver.BASE_API_URL + "/api/user/search")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "6DgFjRZOE94Wd9tIERk79YWJkWjCqvf5JUyKxIuxUgs")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                LinkedTreeMap responseData = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                JsonObject jsonObject = new Gson().toJsonTree(responseData).getAsJsonObject();
                int totalUsers = (int) Double.parseDouble(jsonObject.get("total").getAsJsonPrimitive().toString());
                if(totalUsers == 0) {
                    isPhoneUnique = true;
                }else if(totalUsers == 1){
                    JsonObject firstUser = (JsonObject) jsonObject.get("users").getAsJsonArray().get(0);
                    if(firstUser.getAsJsonPrimitive("id").getAsString().equals(userId)){
                        isPhoneUnique = true;
                    }else isPhoneUnique = false;
                }
                else {
                    isPhoneUnique = false;
                }
            }else isPhoneUnique = true;
        } catch (IOException e) {
            e.printStackTrace();
            isPhoneUnique = true;
        }
        return isPhoneUnique;
    }

    /**
     * Updates the User's profile properties at remote using FusionAuth APIs. The updated properties
     * are provided through the profileElementHolders parameter. This function first make an API
     * call to get the User's current details from remote using {@link BackendCallHelper} and then
     * modify the response to reflect updations and send the updated things back at remote.
     *
     * @param profileElementHolders - A list {@link ProfileElementViewHolders.ProfileElementHolder}s through which updated
     *                              values of a user profile can be accessed.
     */
    @Override
    public void updateUserProfileAtRemote(ArrayList<ProfileElementViewHolders.ProfileElementHolder> profileElementHolders, String fusionAuthKey){
        getMvpView().showLoading("Updating User Profile...");

        String apiKey = ProfileSectionDriver.FUSION_AUTH_API_KEY;
        String userId = ProfileSectionDriver.USER_ID;
        String oldPhone = getContentValueFromKey(profileElementHolders.get(1).getUserProfileElement().getContent());
        String updatedPhone = profileElementHolders.get(1).getUpdatedElementValue();

        String oldEmail = getContentValueFromKey(profileElementHolders.get(2).getUserProfileElement().getContent());
        String updatedEmail = profileElementHolders.get(2).getUpdatedElementValue();

        Single<JSONObject> usersForPhone = getApiHelper().performSearchUserByPhoneCall(updatedPhone, apiKey);
        Single<JSONObject> usersForEmail = getApiHelper().performSearchUserByEmailCall(updatedEmail, apiKey);
        Single<JSONObject> updatedData = getApiHelper()
                .performGetUserDetailsApiCall(userId, apiKey)
                .flatMap(oldData -> {
                    // Update the data with new fields
                    Timber.e(oldData.toString());
                    JSONObject user = oldData.getJSONObject("user");
                    JSONObject internalData;
                    if (user.has("data") && user.getJSONObject("data")!= null) {
                        internalData = user.getJSONObject("data");
                    } else {
                        internalData = new JSONObject();
                    }
                    if(!updatedEmail.equals("") && validateEmailAddress(updatedEmail)){
                        user.put("email", updatedEmail);
                    }
                    if(!updatedPhone.equals("") && validatePhoneNumber(updatedPhone)){
                        user.put("mobilePhone", updatedPhone);
                    }
                    internalData.put("phone", updatedPhone);
                    user.put("data", internalData);
                    oldData.put("user", user);
                    return getApiHelper().performPutUserDetailsApiCall(userId, apiKey, oldData);
                });


        if(oldPhone.equals(updatedPhone) && oldEmail.equals(updatedEmail)){
            updateUserProfile(profileElementHolders, updatedData);
        }
        else if (!oldPhone.equals(updatedPhone)) {
            getCompositeDisposable().add(usersForPhone.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(searchResponse -> {
                        boolean isPhoneUnique = false;
                        int totalUsers = (int) Double.parseDouble(searchResponse.get("total").toString());
                        if(totalUsers == 0) {
                            isPhoneUnique = true;
                        }else if(totalUsers == 1){
                            if(((JSONObject) searchResponse.getJSONArray("users").get(0)).get("id").equals(userId)){
                                isPhoneUnique = true;
                            }else isPhoneUnique = false;
                        }
                        else {
                            isPhoneUnique = false;
                        }

                        if(isPhoneUnique){
                            if (totalUsers == 0) {
                                if (oldEmail.equals(updatedEmail)) {
                                    updateUserProfile(profileElementHolders, updatedData);
                                } else {
                                    getMvpView().hideLoading();
                                    getMvpView().showSnackbar("Both Phone number and email cannot change at the same time. Please try with one first.", 2000);
                                }
                            } else {
                                getMvpView().hideLoading();
                                getMvpView().showSnackbar("Phone number is not unique", 2000);
                            }
                        }else{
                            getMvpView().hideLoading();
                            getMvpView().showSnackbar("Phone number is not unique", 2000);
                        }
                    }, t -> {
                        Timber.e("Error in searching users for phone number");
                        Timber.e(t);
                        getMvpView().hideLoading();
                    }));
        }
        else if (!oldEmail.equals(updatedEmail)) {
            getCompositeDisposable().add(usersForEmail.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(searchResponse -> {
                        int totalUsers = (int) Double.parseDouble(searchResponse.get("total").toString());
                        if (totalUsers == 0) {
                            if (oldPhone.equals(updatedPhone)) {
                                updateUserProfile(profileElementHolders, updatedData);
                            } else {
                                getMvpView().hideLoading();
                                getMvpView().showSnackbar("Both Phone number and email cannot change at the same time. Please try with one first.", 2000);
                            }
                        } else {
                            getMvpView().hideLoading();
                            getMvpView().showSnackbar("Phone number is not unique", 2000);
                        }
                    }, t -> {
                        Timber.e(t);
                        getMvpView().hideLoading();
                    }));
        }else {
            getMvpView().hideLoading();
        }
    }

    private void updateUserProfile(ArrayList<ProfileElementViewHolders.ProfileElementHolder> profileElementHolders, Single<JSONObject> updatedData) {
        getCompositeDisposable().add(updatedData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(newData -> {
                    Timber.d("Success API. Response %s", newData);
                    updateUserProfileLocally(profileElementHolders);
                    getMvpView().hideLoading();
                    getMvpView().showSnackbar("User Details successfully updated.", 5000);
                }, t -> {
                    Timber.e(t);
                    getMvpView().showSnackbar("Failed to update user profile.", 3000);
                    getMvpView().hideLoading();
                }));
    }

    @Override
    public boolean validatePhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("[6-9][0-9]{9}");
        Matcher m = p.matcher(phoneNumber);
        return (m.find() && m.group().equals(phoneNumber));
    }

    @Override
    public boolean validateEmailAddress(String emailAddress) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }

    @Override
    public boolean validateUpdatedFields(ArrayList<ProfileElementViewHolders.ProfileElementHolder> profileElementHolders) {
        int counter = 0;
        for (ProfileElementViewHolders.ProfileElementHolder profileElementHolder : profileElementHolders) {
            if (profileElementHolder.getUserProfileElement().getProfileElementContentType() == UserProfileElement.ProfileElementContentType.PHONE_NUMBER) {
                if (!validatePhoneNumber(profileElementHolder.getUpdatedElementValue())) {
                    Toast.makeText(getMvpView().getActivityContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (counter == 2) {
                if (!validateEmailAddress(profileElementHolder.getUpdatedElementValue())) {
                    Toast.makeText(getMvpView().getActivityContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Fetches the latest value stored against a given key from the {@link android.content.SharedPreferences}.
     * This function uses the {@link ProfileContract.Interactor} to access the data from {@link android.content.SharedPreferences}
     *
     * @param key - The key against which the required content value is stored.
     */
    @Override
    public String getContentValueFromKey(String key) {
        return getMvpInteractor().getActualContentValue(key);
    }

    @Override
    public void onDestroy() {
        getMvpView().hideLoading();
        getCompositeDisposable().dispose();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getMvpView()
                .getActivityContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
