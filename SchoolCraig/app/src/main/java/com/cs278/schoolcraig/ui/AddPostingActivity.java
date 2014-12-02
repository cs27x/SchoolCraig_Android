package com.cs278.schoolcraig.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.NewPost;
import com.cs278.schoolcraig.data.User;
import com.cs278.schoolcraig.mgmt.Preferences;
import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.mgmt.UserManagement;
import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.utils.Utils;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class AddPostingActivity extends Activity {
	private String TAG = getClass().getSimpleName();

    private UserManagement userMgmt = null;
	private PostingAdapter adapter = null;
	
	private EditText postingTitle;
	private EditText postingDescription;
    private EditText postingPrice;
	private Spinner postingCategory;

	private String newPostingTitle;
	private String newPostingDescription;
    private double newPostingPrice;
    private String newPostingPoster;
	private String newPostingCategory;
    private String newPostingCreationDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        adapter = PostingAdapter.getInstance(getApplicationContext());
        userMgmt = UserManagement.getInstance(getApplicationContext());
		setContentView(R.layout.activity_add_posting);
		postingTitle = (EditText)this.findViewById(R.id.add_posting_title);
		postingDescription = (EditText)this.findViewById(R.id.add_posting_description);
        postingPrice = (EditText) this.findViewById(R.id.add_posting_price);
		postingCategory = (Spinner)this.findViewById(R.id.add_posting_category);
        Preferences.getInstance().Initialize(this.getApplicationContext());
		setupSpinnerForCategories();
	}
	
	private void setupSpinnerForCategories() {
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapter.categories);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		postingCategory.setAdapter(categoryAdapter);
	}
	
    public void addNewPostingDoneButtonClick(View view) {
        this.newPostingTitle = this.postingTitle.getText().toString();
        this.newPostingDescription = this.postingDescription.getText().toString();
        View focusView = getViewWithErrorIfThereIsOne();
        // Error with one of the fields
        if(focusView != null) {
            focusView.requestFocus();
        } else {
            this.newPostingPrice = Double.parseDouble(this.postingPrice.getText().toString());
            this.newPostingPoster = userMgmt.getCurrentUserEmail();
            this.newPostingCategory = this.postingCategory.getSelectedItem().toString();
            Calendar now = Calendar.getInstance();
            this.newPostingCreationDate = Utils.sdf.format(now.getTime());
            Log.d("Creation Date", newPostingCreationDate);
            addValidNewPosting();
            finish();
        }
    }

	private void addValidNewPosting() {

        Log.d("ADDPOST", "");

        final NewPost newPost = new NewPost(newPostingPrice,
                userMgmt.getCurrentUserId(),
                newPostingTitle,
                newPostingDescription,
                Preferences.getInstance().getSavedValue(newPostingCategory),
                newPostingCreationDate);

        final SchoolCraigAPI api = RestClient.get();

        CallableTask.invoke(new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    api.createPost(newPost);
                                    return null;
                                }
                            }, new TaskCallback<Void>() {
                                @Override
                                public void success(Void result) {
                                    Log.d("SUCCESS", "post stored");

                                    backToPostingListActivity();
                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );


	}

    private void backToPostingListActivity() {
        Intent intent = new Intent(AddPostingActivity.this, PostingListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
        this.startActivity(intent);
    }
    
    private View getViewWithErrorIfThereIsOne() {
		// Check valid entries. Title not empty, location not empty and valid address,
		// description not empty, date not before current date, category must be selected
 
    	// Reset errors
    	this.postingTitle.setError(null);
    	this.postingDescription.setError(null);
        this.postingPrice.setError(null);

    	View focusView = null;
    	if(TextUtils.isEmpty(this.newPostingTitle)) {
    		postingTitle.setError(getString(R.string.error_field_required));
    		focusView = postingTitle;
    	}
    	if(TextUtils.isEmpty(this.newPostingDescription)) {
    		postingDescription.setError(getString(R.string.error_field_required));
    		focusView = postingDescription;
    	}
        if(TextUtils.isDigitsOnly(this.postingPrice.getText())) {
            postingPrice.setError(getString(R.string.error_field_required));
            focusView = postingPrice;
        }
	    return focusView;
    }
}
