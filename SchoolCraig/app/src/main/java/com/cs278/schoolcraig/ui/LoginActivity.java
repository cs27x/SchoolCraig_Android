package com.cs278.schoolcraig.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.cs278.schoolcraig.mgmt.Preferences;
import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.mgmt.UserManagement;
import com.cs278.schoolcraig.data.Auth;
import com.cs278.schoolcraig.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>{
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserManagement userMgmt = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Preferences.getInstance().Initialize(this.getApplicationContext());

        userMgmt = UserManagement.getInstance(getApplicationContext());

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserLoggedInOrContinue();
    }

    public void onClickRegisterUserButton(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        String email = mEmailView.getText().toString();
        String pword = mPasswordView.getText().toString();
        if(!TextUtils.isEmpty(email))
            intent.putExtra("email", email);
        if(!TextUtils.isEmpty(pword))
            intent.putExtra("pword", pword);
        startActivity(intent);
    }

    public void onClickLoginAttempt(View view) {
        attemptLogin();
    }

    private void checkUserLoggedInOrContinue() {
        if(userMgmt.userDetailsExist()) {
            Intent intent = new Intent(getApplicationContext(), PostingListActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!isVandyEmail(email)) {
            mEmailView.setError(getString(R.string.error_not_vandy_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            final Auth auth = new Auth(email, password);
            final SchoolCraigAPI api = RestClient.get();

            CallableTask.invoke(new Callable<User>() {
                                    @Override
                                    public User call() throws Exception {
                                        return api.authUser(auth);
                                    }
                                }, new TaskCallback<User>() {
                                    @Override
                                    public void success(User user) {
                                        Log.d("SUCCESS", "user authenticated");
                                        showProgress(false);

                                        userMgmt = UserManagement.getInstance(LoginActivity.this);
                                        userMgmt.addUserId(user.getId());
                                        userMgmt.addUserEmail(user.getEmail());
                                        userMgmt.addUserFname(user.getFname());
                                        userMgmt.addUserLname(user.getLname());

                                        startPostingListActivity();
                                    }

                                    @Override
                                    public void error(Exception e) {
                                        Log.d("ERROR", e.getMessage().toString());
                                        showProgress(false);
                                        showUnauthorizedMsg();
                                    }
                                }
            );
        }
    }

    private void startPostingListActivity() {
        Intent intent = new Intent(LoginActivity.this, PostingListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
        this.startActivity(intent);
    }

    private void showUnauthorizedMsg(){

        mEmailView.setError(null);

        mEmailView.setError(getString(R.string.error_unauthorized));
        View focusView = mEmailView;
        focusView.requestFocus();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isVandyEmail(String email) {
        return email.contains("@vanderbilt.edu");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
}