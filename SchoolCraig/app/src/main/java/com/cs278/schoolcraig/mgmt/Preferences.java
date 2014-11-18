package com.cs278.schoolcraig.mgmt;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by violettavylegzhanina on 11/17/14.
 */
public class Preferences {

    private static final String APP_SHARED_PREFS = "appdetails";
    private static Preferences mInstance;
    private Context mContext;

    private SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;

    private Preferences(){ }

    public static Preferences getInstance(){
        if(mInstance == null)
            mInstance = new Preferences();

        return mInstance;
    }

    public void Initialize(Context c){
        mContext = c;
        mPreferences = mContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void writePreference(String key, String value){

        if(value == null)
            return;

        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getSavedValue(String key){
        return mPreferences.getString(key, null);
    }

    public boolean detailsExist(String key){
        return mPreferences.contains(key);
    }

    public void clear(){
        mEditor.clear();
        mEditor.commit();
    }
}
