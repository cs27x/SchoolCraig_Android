package com.cs278.schoolcraig.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.UserManagement;
import com.cs278.schoolcraig.data.Posting;

import java.util.Calendar;

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
            this.newPostingCreationDate = Posting.sdf.format(now.getTime());
            addValidNewPosting();
            finish();
        }
    }

	private void addValidNewPosting() {
		Posting newPosting = new Posting(newPostingTitle, newPostingDescription, newPostingPrice, newPostingPoster, newPostingCategory, newPostingCreationDate);
        // TODO remove next line once connected via API because on reloading the list view it will update with new posting
		adapter.addEvent(newPosting);
        newPosting.addPostingViaAPI();
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
