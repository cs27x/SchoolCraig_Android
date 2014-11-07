package com.cs278.schoolcraig.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.UserManagement;
import com.cs278.schoolcraig.UserRegisterLoginTask;
import com.cs278.schoolcraig.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class RegisterActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    //    private UserRegisterTask mRegisterTask = null;
    private UserRegisterLoginTask mRegLoginTask = null;
    private UserManagement userMgmt = null;
    private View mProgressView = null;
    private View mRegisterFormView = null;

    private EditText regFirstName = null;
    private EditText regLastName = null;
    private AutoCompleteTextView regEmail;
    private EditText regPword = null;
    private EditText regPwordVerify = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userMgmt = UserManagement.getInstance(getApplicationContext());

        regFirstName = (EditText) findViewById(R.id.reg_first_name);
        regLastName = (EditText) findViewById(R.id.reg_last_name);
        regEmail = (AutoCompleteTextView) findViewById(R.id.reg_email);
        populateAutoComplete();
        regPword = (EditText) findViewById(R.id.reg_password);
        regPwordVerify = (EditText) findViewById(R.id.reg_password_verify);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        getAnyInfoFromIntent(getIntent());
    }

    private void getAnyInfoFromIntent(Intent intent) {
        if (intent.hasExtra("email"))
            regEmail.setText(intent.getStringExtra("email"));
        if (intent.hasExtra("pword")) {
            String pword = intent.getStringExtra("pword");
            regPword.setText(pword);
            regPwordVerify.setText(pword);
        }
    }

    public void onClickRegisterButton(View v) {
        attemptRegister();
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister() {
//        if (mRegisterTask != null) {
//            return;
//        }

        if (mRegLoginTask != null) {
            return;
        }

        // Reset errors.
        regFirstName.setError(null);
        regLastName.setError(null);
        regEmail.setError(null);
        regPword.setError(null);
        regPwordVerify.setError(null);

        // Store values at the time of the login attempt.
        String firstName = regFirstName.getText().toString();
        String lastName = regLastName.getText().toString();
        String email = regEmail.getText().toString();
        String password = regPword.getText().toString();
        String passwordVerify = regPwordVerify.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            regPword.setError(getString(R.string.error_field_required));
            focusView = regPword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            regPword.setError(getString(R.string.error_invalid_password));
            focusView = regPword;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            regPwordVerify.setError(getString(R.string.error_field_required));
            focusView = regPwordVerify;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            regPwordVerify.setError(getString(R.string.error_invalid_password));
            focusView = regPwordVerify;
            cancel = true;
        } else if (!password.equals(passwordVerify)) {
            regPword.setError(getString(R.string.error_mismatch_passwords));
            regPwordVerify.setError(getString(R.string.error_mismatch_passwords));
            focusView = regPwordVerify;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            regEmail.setError(getString(R.string.error_field_required));
            focusView = regEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            regEmail.setError(getString(R.string.error_invalid_email));
            focusView = regEmail;
            cancel = true;
        } else if (!isVandyEmail(email)) {
            regEmail.setError(getString(R.string.error_not_vandy_email));
            focusView = regEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            regFirstName.setError(getString(R.string.error_field_required));
            focusView = regFirstName;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            regLastName.setError(getString(R.string.error_field_required));
            focusView = regLastName;
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

//            mRegisterTask = new UserRegisterTask(firstName, lastName, email, password);
//            mRegisterTask.execute((Void) null);
            final User user = new User(firstName, lastName, email, password);
            final SchoolCraigAPI api = RestClient.get();

            CallableTask.invoke(new Callable<Void>() {
                                    @Override
                                    public Void call() throws Exception {
                                        api.createUser(user);
                                        return null;
                                    }
                                }, new TaskCallback<Void>() {
                                    @Override
                                    public void success(Void result) {
                                        Log.d("SUCCESS", "user stored");
                                        showProgress(false);

                                        backToLoginActivity();
                                    }

                                    @Override
                                    public void error(Exception e) {
                                        Log.d("ERROR", e.getMessage().toString());
                                        showProgress(false);
                                    }
                                }
            );

//            mRegLoginTask = new UserRegisterLoginTask(RegisterActivity.this, user);
//            ////mRegLoginTask = new UserRegisterLoginTask(RegisterActivity.this, firstName, lastName, email, password);
//            mRegLoginTask.execute(Utils.REGISTER);
        }
    }

    private void backToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
        this.startActivity(intent);
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
                new ArrayAdapter<String>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        regEmail.setAdapter(adapter);
    }

    private void startPostingListActivityFinishRegister() {
        Intent intent = new Intent(getApplicationContext(), PostingListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}
