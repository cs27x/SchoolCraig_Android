package com.cs278.schoolcraig;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alex on 10/23/2014.
 */
public class UserManagement {
    private static final String APP_SHARED_PREFS = "userdetails";
    private static final String USER_EMAIL = "email";
    private static UserManagement instance = null;
    private static Context appContext = null;
    private static SharedPreferences userDetails = null;
    private static SharedPreferences.Editor editor = null;

    protected UserManagement(Context context) {
        appContext = context;
        userDetails = appContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        editor = userDetails.edit();
    }

    public static UserManagement getInstance(Context context) {
        if (instance == null)
            instance = new UserManagement(context);
        return instance;
    }

    public void addUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public String getCurrentUserEmail() {
        return userDetails.getString(USER_EMAIL, "");
    }

    public void clearUserDetails() {
        editor.clear();
        editor.commit();
    }

    public boolean userDetailsExist() {
        return userDetails.contains(USER_EMAIL);
    }
}
