package com.cs278.schoolcraig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PostingListFragment} and the item details
 * (if present) is a {@link PostingDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link PostingListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PostingListActivity extends Activity
        implements PostingListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private UserManagement userMgmt = null;
    private PostingAdapter postAdapter = null;
    public static boolean filtered = false;
    private Button addPostingButton;
    private Button filterPostingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userMgmt = UserManagement.getInstance(getApplicationContext());
        postAdapter = PostingAdapter.getInstance(getApplicationContext());
        setContentView(R.layout.activity_posting_list);
        addPostingButton = (Button)findViewById(R.id.add_posting_button);
        filterPostingButton = (Button)findViewById(R.id.filter_posting_button);
        addClickListeners();

        if (findViewById(R.id.posting_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((PostingListFragment) getFragmentManager()
                    .findFragmentById(R.id.posting_list))
                    .setActivateOnItemClick(true);
        }
    }
    
    private void addClickListeners() {
    	addPostingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddPostingActivity.class);
                startActivity(intent);
            }
        });
        
    	filterPostingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtered = true;
                Intent intent = new Intent(v.getContext(), FilterPostingListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Callback method from {@link PostingListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int index) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(PostingDetailFragment.ARG_POSTING_INDEX, index);
            PostingDetailFragment fragment = new PostingDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.posting_detail_container, fragment)
                    .commit();
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, PostingDetailActivity.class);
            detailIntent.putExtra(PostingDetailFragment.ARG_POSTING_INDEX, index);
            startActivity(detailIntent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refreshmenu, menu);
        inflater.inflate(R.menu.listmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userMgmt.clearUserDetails();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);
                return true;
            case R.id.update:
                postAdapter.loadDataFromBackendUsingAPI(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
