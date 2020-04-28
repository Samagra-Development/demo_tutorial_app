package com.samagra.commons;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import timber.log.Timber;

/**
 * This class contains the common utilities that can be used independently in any module using this library.
 * All the functions in this class must be public static.
 *
 * @author Pranav Sharma
 */
public class CommonUtilities {
    /**
     * Starts activity as a new task. This means all the activities in the current Task will be removed.
     * Basically, clears the back stack.
     *
     * @param intent  - The {@link Intent} repsonsible for the new activity
     * @param context - The {@link Context} for the current Activity.
     */
    public static void startActivityAsNewTask(Intent intent, @NonNull Context context) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    /**
     * This functions takes the current {@link Context} and tells if the device is connected to the internet.
     *
     * @param context - Non-null {@link Context} of calling Activity.
     * @return boolean - true if internet available, false otherwise.
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            Timber.e("ConnectivityManager is null");
            return false;
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
