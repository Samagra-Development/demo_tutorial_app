package com.samagra.ancillaryscreens.screens.passReset;

import android.os.AsyncTask;


import com.samagra.ancillaryscreens.AncillaryScreensDriver;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class SendOTPTask extends AsyncTask<String, Void, String> {

    private ChangePasswordActionListener listener;
    private String TAG = SendOTPTask.class.getName();
    private boolean isPhoneUnique;

    public SendOTPTask(ChangePasswordActionListener listener){
        this.listener = listener;
    }

    private boolean testIfPhoneUnique(String phone){
        return true;
    }

    @Override
    protected String doInBackground(String[] strings) {
        isPhoneUnique = testIfPhoneUnique(strings[0]);
        if (!isPhoneUnique) {
            listener.onFailure(new Exception("Multiple users with the same phone number found. Contact Admin."));
            return null;
        } else {
            String serverURL = AncillaryScreensDriver.SEND_OTP_URL;
            String phoneNo = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(serverURL + "send-OTP?phoneNo=" + phoneNo)
                    .get()
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Timber.d(TAG, "Successful Response");
                    return response.body().toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    protected void onPostExecute(String s){
        if(isPhoneUnique) {
            listener.onSuccess();
        }
    }
}
