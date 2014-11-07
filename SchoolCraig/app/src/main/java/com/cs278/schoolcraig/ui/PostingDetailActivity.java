package com.cs278.schoolcraig.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.cs278.schoolcraig.ui.PostingDetailFragment;
import com.cs278.schoolcraig.ui.PostingListActivity;
import com.cs278.schoolcraig.R;


/**
 * An activity representing a single Event detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link com.cs278.schoolcraig.ui.PostingListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link com.cs278.schoolcraig.ui.PostingDetailFragment}.
 */
public class PostingDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
        	int postingIndex = getIntent().getIntExtra(PostingDetailFragment.ARG_POSTING_INDEX, 0);
            Bundle arguments = new Bundle();
            arguments.putInt(PostingDetailFragment.ARG_POSTING_INDEX, postingIndex);
            PostingDetailFragment fragment = new PostingDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.posting_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, PostingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}