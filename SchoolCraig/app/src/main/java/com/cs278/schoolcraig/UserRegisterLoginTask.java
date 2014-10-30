package com.cs278.schoolcraig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.util.Log;
import android.view.View;

/**
 * Created by violettavylegzhanina on 10/30/14.
 */

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserRegisterLoginTask extends AsyncTask<Void, Void, Boolean> {

    private UserManagement userMgmt = null;

    //private Context context;
    private Activity mActivity;
    private ProgressDialog dialog;

    private String mFirstName = null;
    private String mLastName = null;
    private String mEmail = null;
    private String mPassword = null;

    UserRegisterLoginTask(Activity a, String firstName, String lastName, String email, String password) {
        mActivity = a;
        dialog = new ProgressDialog(a);
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mPassword = password;
    }

    UserRegisterLoginTask(Activity a, String email, String password) {
        mActivity = a;
        dialog = new ProgressDialog(a);
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected void onPreExecute(){
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO register new user via API

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        //showProgress(false);
        dialog.dismiss();
        if (success) {
            userMgmt = UserManagement.getInstance(mActivity);
            userMgmt.addUserEmail(mEmail);

            startPostingListActivityFinishRegister();
        } else {
//            regEmail.setError(mActivity.getString(R.string.error_incorrect_password));
//            regEmail.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        //showProgress(false);
        dialog.dismiss();
    }

    private void startPostingListActivityFinishRegister() {
        Intent intent = new Intent(mActivity, PostingListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.finish();
        mActivity.startActivity(intent);
    }

}
