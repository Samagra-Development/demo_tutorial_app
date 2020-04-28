package com.samagra.notification_module;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.Date;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;


public class PushMessagingService extends FirebaseMessagingService {

    static final String TAG = "PushMessagingService";
    private static final int mSAMWAD_NOTIFICATION_ID = 1328974929;
    public static final int FORMS_UPLOADED_NOTIFICATION = 97;


    // When the app starts it takes time to start the service and hence the context might not be there.
    public Context externalContext;
    static String authURL;
    static String apiKey;

    public PushMessagingService setContext(Context context, String authURL, String apiKey){
        this.externalContext = context;
        PushMessagingService.authURL = authURL;
        PushMessagingService.apiKey = apiKey;
        return this;
    }

    public void getCurrentToken(Context context){
        this.externalContext = context;
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        persistToken(token);
                        Log.d(TAG, "Current Token: " + token);
                    }
                });
    }

    private void persistToken(String token) {
        persistTokenLocally(token);
        persistTokenToServer(token);
    }

    private void persistTokenLocally(String token) {
        Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("FCM.token", token);
        editor.apply();
    }

    private void persistTokenToServer(final String token) {
        final Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPreferences.getBoolean("isLoggedIn", false)){

            // Fixme: Refactor and Execute on threadpool
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String userId = sharedPreferences.getString("user.id", "");
                        OkHttpClient client = new OkHttpClient();
                        //Grove.e(TAG, "" + authURL);
                        Request request = new Request.Builder()
                                .url(authURL + "/api/user/" + userId)
                                .get()
                                .addHeader("Authorization", apiKey)
                                .build();

                        Response response;
                        try {
                            response = client.newCall(request).execute();
                            if(response.isSuccessful()){
                                LinkedTreeMap responseData = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                                JsonObject jsonObject = new Gson().toJsonTree(responseData).getAsJsonObject();
                                //Grove.e(TAG, "User1" + jsonObject.get("user"));
                                JsonObject user = jsonObject.get("user").getAsJsonObject();
                                // Edit data here.
                                JsonObject data;
                                try{
                                    data = user.get("data").getAsJsonObject();
                                }catch (Exception e){
                                    data = new JsonObject();
                                }
                                data.addProperty("FCM.token", token);
                                user.add("data", data);
                                responseData.put("user", user);

                                Gson gsonObj = new Gson();
                                String jsonStr = gsonObj.toJson(responseData);
                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType, jsonStr);
                                Request reqForUpdate = new Request.Builder()
                                        .url(authURL + "/api/user/" + userId)
                                        .put(body)
                                        .addHeader("Authorization", apiKey)
                                        .build();
                                response = client.newCall(reqForUpdate).execute();
                                //Grove.e(TAG, "Response" + response.body().string());
                                if(response.isSuccessful()){
                                    //Grove.e(TAG, "Token addition successful");
                                }else{
                                    //Grove.e(TAG, "Token addition failed");
                                }
                            }else{
                               // Grove.e(TAG, "Token addition failed");
                            }
                        } catch (IOException e) {
                           // Grove.e(e);
                           // Grove.e(TAG, "Token addition failed");
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                        //Grove.e(e.getMessage());
                       // Grove.e(e);
                    }

                }
            });

        }
    }

    public void removeTokenFromServer(String token){
        final Context context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Fixme: Refactor and Execute on threadpool
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String userId = sharedPreferences.getString("user.id", "");
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(authURL + "/api/user/" + userId)
                        .get()
                        .addHeader("Authorization", apiKey)
                        .build();

                Response response;
                try {
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        LinkedTreeMap responseData = new Gson().fromJson(response.body().string(), LinkedTreeMap.class);
                        JsonObject jsonObject = new Gson().toJsonTree(responseData).getAsJsonObject();
                       // Grove.e(TAG, "User1" + jsonObject.get("user"));
                        JsonObject user = jsonObject.get("user").getAsJsonObject();
                        JsonObject data = user.get("data").getAsJsonObject();
                        data.addProperty("FCM.token", "");
                        user.add("data", data);
                        responseData.put("user", user);

                        Gson gsonObj = new Gson();
                        String jsonStr = gsonObj.toJson(responseData);
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, jsonStr);
                        Request reqForUpdate = new Request.Builder()
                                .url(authURL + "/api/user/" + userId)
                                .put(body)
                                .addHeader("Authorization", apiKey)
                                .build();
                        response = client.newCall(reqForUpdate).execute();
                        //Grove.e(TAG, "Response" + response.body().string());
                        if(response.isSuccessful()){
                           // Grove.e(TAG, "Token removed successfully");
                        }else{
                           // Grove.e(TAG, "Token removal failed");
                        }
                    }else{
                       // Grove.e(TAG, "Token removal failed");
                    }
                } catch (IOException e) {
                   // Grove.e(e);
                   // Grove.e(TAG, "Token removal failed");
                    e.printStackTrace();
                }
            }
        });
    }

    private Context getContext() {
        Context context;
        if (externalContext == null) {
            context = PushMessagingService.this;
        } else context = externalContext;
        return context;
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Timber.d("Refreshed token: " + token);
        persistToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent notifyIntent = new Intent(externalContext, NotificationRenderingActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notifyIntent.putExtra(NotificationRenderingActivity.NOTIFICATION_TITLE, externalContext.getString(R.string.upload_results));
        notifyIntent.putExtra(NotificationRenderingActivity.NOTIFICATION_MESSAGE, remoteMessage.getNotification().getBody());
        PendingIntent pendingNotify = PendingIntent.getActivity(externalContext, FORMS_UPLOADED_NOTIFICATION,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AppNotificationUtils.showNotification(externalContext,
                pendingNotify,
                (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                Log.d(TAG, "Message Handled");
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {
    }
}
