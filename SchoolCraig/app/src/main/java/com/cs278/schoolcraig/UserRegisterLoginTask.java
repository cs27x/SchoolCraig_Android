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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by violettavylegzhanina on 10/30/14.
 */

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserRegisterLoginTask extends AsyncTask<Integer, Void, Boolean> {

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
    protected Boolean doInBackground(Integer... params) {
        // TODO register new user via API

        Integer taskType = params[0];
        HttpResponse response = null;

        switch(taskType) {

            case(Utils.REGISTER):

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Utils.CREATE_USER);

            ArrayList<NameValuePair> myList = Utils.makeRegisterList(mFirstName, mLastName, mEmail, mPassword);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(myList));
                Log.d("Register", "set entity");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                response = httpclient.execute(httpPost);
                Log.d("Register", "executed response");
            } catch (ClientProtocolException e) {
                Log.e("Clientprotocolexception", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
                e.printStackTrace();
            }

            case Utils.LOGIN:
        }

        Log.d("Response", response.getStatusLine().getStatusCode()+"");
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
