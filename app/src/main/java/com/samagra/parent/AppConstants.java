package com.samagra.parent;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;

/**
 * These are constants required by the app module. All values declared should be public static and the constants should
 * preferably be final constants.
 *
 * @author Pranav Sharma
 */
public class AppConstants {
    public static final String PREF_FILE_NAME = "SAMAGRA_PREFS";
    public static final String BASE_API_URL = "http://134.209.150.161:9011";
    public static final String ROOT = Environment.getExternalStorageDirectory()
            + File.separator + "odk";
    public static final String FILE_PATH =  Environment.getExternalStorageDirectory()
            + File.separator + "odk" + "/data2.json";
    public static final String SEND_OTP_URL = "http://localhost:8080/shiksha-saathi/";
    public static final String UPDATE_PASSWORD_URL = "http://localhost:8080/shiksha-saathi/";
    public static String ABOUT_WEBSITE_LINK = "https://samagra-development.github.io/docs/";
    public static String ABOOUT_FORUM_LINK = "https://samagra-development.github.io/docs/";

}
