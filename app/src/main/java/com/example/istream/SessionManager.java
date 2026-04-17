package com.example.istream;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME = "istream_session";
    private static final String KEY_USERNAME = "logged_in_username";

    public static void saveSession(Context context, String username) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_USERNAME, username).apply();
    }

    public static String getLoggedInUsername(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_USERNAME, null);
    }

    public static void clearSession(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().remove(KEY_USERNAME).apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getLoggedInUsername(context) != null;
    }
}
