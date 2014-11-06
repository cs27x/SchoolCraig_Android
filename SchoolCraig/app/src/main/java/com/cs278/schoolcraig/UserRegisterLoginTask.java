package com.cs278.schoolcraig;

import android.content.Intent;
import android.os.AsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.cs278.schoolcraig.data.User;
import com.cs278.schoolcraig.ui.PostingListActivity;
import com.cs278.schoolcraig.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

//    private String mFirstName = null;
//    private String mLastName = null;
//    private String mEmail = null;
//    private String mPassword = null;

    private HttpResponse response = null;
    private DefaultHttpClient httpclient;
    private HttpPost httpPost;
    private User mUser;
    private Gson gson;
    private String json;

//    UserRegisterLoginTask(Activity a, String firstName, String lastName, String email, String password) {
//        mActivity = a;
//        dialog = new ProgressDialog(a);
//        mFirstName = firstName;
//        mLastName = lastName;
//        mEmail = email;
//        mPassword = password;
//    }
//
//    UserRegisterLoginTask(Activity a, String email, String password) {
//        mActivity = a;
//        dialog = new ProgressDialog(a);
//        mEmail = email;
//        mPassword = password;
//    }

    UserRegisterLoginTask(Activity a, User u){

        mActivity = a;
        dialog = new ProgressDialog(a);
        mUser = u;

        Log.d("Task", "constructor called");
    }

    @Override
    protected void onPreExecute(){
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        // TODO register new user via API

        Integer taskType = params[0];

        switch(taskType) {

            case(Utils.REGISTER):

//                RestClient.get().createUser(mUser, new TaskCallback<UserResponse>() {
//                    @Override
//                    public void success(UserResponse result) {
//                        Log.d("SUCCESS", result.getFname());
//                    }
//
//                    @Override
//                    public void error(Exception e) {
//                        Log.d("ERROR", e.getMessage().toString());
//                    }
//                });



//            httpclient = new DefaultHttpClient();
//            httpPost = new HttpPost(Utils.CREATE_USER);
//
//            //ArrayList<NameValuePair> myList = Utils.makeRegisterList(mFirstName, mLastName, mEmail, mPassword);
//            //User user = new User(mFirstName, mLastName, mEmail, mPassword);
//            gson = new Gson();
//            json = gson.toJson(mUser);
//                Log.d("JSON", json);
//
//            try {
//                httpPost.setEntity(new StringEntity(json, "UTF8"));
//                Log.d("Register", "set entity");
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            try {
//                response = httpclient.execute(httpPost);
//                Log.d("Register", "executed response");
//
//                Log.d("Response", response.getStatusLine().getStatusCode()+"");
//                HttpEntity entity = response.getEntity();
////                String content = EntityUtils.toString(entity);
////                Log.d("Response", content);
//                InputStream is = response.getEntity().getContent();
//                String body = IOUtils.toString(is, "UTF8");
//                Log.d("Response", body);
//
//            } catch (ClientProtocolException e) {
//                Log.e("Clientprotocolexception", e.toString());
//                e.printStackTrace();
//            } catch (IOException e) {
//                Log.e("IOException", e.toString());
//                e.printStackTrace();
//            }
                break;

            case Utils.LOGIN:

                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost(Utils.AUTH_USER);

                //ArrayList<NameValuePair> myList = Utils.makeRegisterList(mFirstName, mLastName, mEmail, mPassword);
                //user = new User(mEmail, mPassword);
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                json = gson.toJson(mUser);
                Log.d("JSON", json);

                try {
                    httpPost.setEntity(new StringEntity(json, "UTF8"));
                    Log.d("Login", "set entity");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                try {
                    response = httpclient.execute(httpPost);
                    Log.d("Login", "executed response");

                    Log.d("Login", response.getStatusLine().getStatusCode()+"");
                    HttpEntity entity = response.getEntity();
                    InputStream is = response.getEntity().getContent();
                    String body = IOUtils.toString(is, "UTF8");
                    Log.d("Response", body);

                } catch (ClientProtocolException e) {
                    Log.e("Clientprotocolexception", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("IOException", e.toString());
                    e.printStackTrace();
                }
                break;

            default:

        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        //showProgress(false);
        dialog.dismiss();
        if (success) {
            userMgmt = UserManagement.getInstance(mActivity);
            userMgmt.addUserEmail(mUser.getEmail());

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
