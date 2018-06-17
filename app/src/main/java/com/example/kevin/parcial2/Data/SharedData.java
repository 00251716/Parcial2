package com.example.kevin.parcial2.Data;

import android.content.Context;
import android.content.Intent;

import com.example.kevin.parcial2.Activities.LoginActivity;
import com.example.kevin.parcial2.ModelsAndEntities.User;

public class SharedData {

    private static final String PREF_NAME = "APP_SETTINGS";

    //Se utiliza para verificar si el usuario est� logeado en la LoginActivity
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_USERNAME = "KEY_USERNAME";
    public static final String KEY_TOKEN = "KEY_TOKEN";

    private static Context mContext;

    private static android.content.SharedPreferences preferences;
    private static android.content.SharedPreferences.Editor editor;

    public static void init(Context context) {
        mContext = context;
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.commit();
        }
    }


    public static String read(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static void write(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void write(String key, String value) {
        editor.putString(key, value);
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

    public static boolean checkLogin(){
        if(!isLoggedIn()){
            Intent i = new Intent(mContext, LoginActivity.class);

            //Close all the activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            mContext.startActivity(i);
            return true;
        }
        return false;
    }

    public static void logOutUser(){
        editor.clear();
        editor.commit();
        checkLogin();
    }
}
