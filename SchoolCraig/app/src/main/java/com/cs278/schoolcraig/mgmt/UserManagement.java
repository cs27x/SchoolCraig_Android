package com.cs278.schoolcraig.mgmt;

import android.content.Context;

/**
 * Created by Alex on 10/23/2014.
 */
public class UserManagement {
    private static final String APP_SHARED_PREFS = "userdetails";
    private static final String USER_EMAIL = "email";
    private static final String USER_ID = "id";
    private static final String USER_FNAME = "fname";
    private static final String USER_LNAME = "lname";
    private static UserManagement instance = null;

    protected UserManagement(Context context) {
        Preferences.getInstance().Initialize(context);
    }

    public static UserManagement getInstance(Context context) {
        if (instance == null)
            instance = new UserManagement(context);
        return instance;
    }

    public void addUserEmail(String email) {
        Preferences.getInstance().writePreference(USER_EMAIL, email);
    }

    public void addUserFname(String fname){
        Preferences.getInstance().writePreference(USER_FNAME, fname);
    }

    public void addUserLname(String lname){
        Preferences.getInstance().writePreference(USER_LNAME, lname);
    }

    public void addUserId(String id) {
        Preferences.getInstance().writePreference(USER_ID, id);
    }

    public String getCurrentUserEmail() {
        return Preferences.getInstance().getSavedValue(USER_EMAIL);
    }

    public String getCurrentUserFname(){
        return Preferences.getInstance().getSavedValue(USER_FNAME);
    }

    public String getCurrentUserLname(){
        return Preferences.getInstance().getSavedValue(USER_LNAME);
    }

    public String getCurrentUserId() {

        return Preferences.getInstance().getSavedValue(USER_ID);
    }

    public void clearUserDetails() {
        Preferences.getInstance().clear();
    }

    public boolean userDetailsExist() {
        return Preferences.getInstance().detailsExist(USER_EMAIL);
    }
}
