package com.example.kevin.parcial2.Persistence;

import android.content.Context;

import com.example.kevin.parcial2.Entities.User;

public class SharedData {

    private static final String PREF_NAME = "APP_SETTINGS";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_TOKEN = "KEY_TOKEN";

    private static android.content.SharedPreferences preferences;
    private static android.content.SharedPreferences.Editor editor;

    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.commit();
        }
    }

    public static void logOut() {
        editor.clear();
        editor.commit();
    }

    public static boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static boolean hasUserDetail() {
        return !preferences.getString(KEY_USER_ID, "").isEmpty();
    }

    public static void setUserDetail(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUser());
        editor.commit();
    }

    public static String getUserId() {
        return  preferences.getString(KEY_USER_ID, "");
    }

    public static String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public static void setToken(String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public static String getToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

}
