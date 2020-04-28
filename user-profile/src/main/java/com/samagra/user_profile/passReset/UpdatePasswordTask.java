package com.samagra.user_profile.passReset;

import android.os.AsyncTask;

import com.samagra.user_profile.ProfileSectionDriver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePasswordTask extends AsyncTask<String, Void, String> {

    private ActionListener listener;
    private String TAG = UpdatePasswordTask.class.getName();
    private boolean isSuccessful = true;

    public UpdatePasswordTask(ActionListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String[] strings) {
        String serverURL = ProfileSectionDriver.RESET_PASSWORD_URL;
        String phoneNo = strings[0];
        String otp = strings[1];
        String password = strings[2];

        JSONObject requestJson = new JSONObject();
        try {
            // Add values to json
            requestJson.put("phoneNo", phoneNo);
            requestJson.put("otp", otp);
            requestJson.put("password", password);
            requestJson.put("applicationId", ProfileSectionDriver.applicationID);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, requestJson.toString());
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(serverURL + "change-password")
                    .post(body)
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    return response.body().string();

                }else{
                    isSuccessful = false;
                    String jsonData = response.body().string();
                    JSONObject responseObject = new JSONObject(jsonData);
                    return responseObject.getString("status");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String s){
        if(!isSuccessful) listener.onFailure(new Exception(s));
        else listener.onSuccess();
    }
}
