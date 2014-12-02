package com.cs278.schoolcraig.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.mgmt.Preferences;
import com.cs278.schoolcraig.mgmt.UserManagement;
import com.cs278.schoolcraig.ui.PostingDetailFragment;
import com.cs278.schoolcraig.ui.PostingListActivity;
import com.cs278.schoolcraig.R;

import java.util.Collection;
import java.util.concurrent.Callable;


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

    PostingDetailFragment fragment;

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
            fragment = new PostingDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.posting_detail_container, fragment)
                    .commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

//        View v  = fragment.getView();
//        TextView tv = (TextView) v.findViewById(R.id.posting_detail_poster);
//        Log.d("EMAIL TEST", tv.getText().toString());

        Post p = fragment.getmPosting();
        UserManagement m = UserManagement.getInstance(fragment.getActivity().getApplicationContext());
        // make sure a user can only delete his post
        if(p.getUser().getEmail().equals(m.getCurrentUserEmail())) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.deletemenu, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, PostingListActivity.class));
                return true;
            case R.id.delete:
                // TODO verify if a user wants to delete a post

                return deletePost();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean deletePost(){

        final Post toDelete = fragment.getmPosting();
        final SchoolCraigAPI api = RestClient.get();
        CallableTask.invoke(new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    api.deletePost(toDelete.getId());
                                    return null;
                                }
                            }, new TaskCallback<Void>() {

                                @Override
                                public void success(Void result) {
                                    Log.d("SUCCESS", "post deleted");
                                    // if Activity is filtered, we won't reload data
                                    // so delete post from adapter
                                    if (PostingListActivity.filtered){
                                        PostingAdapter.getInstance(fragment.getActivity().getApplicationContext())
                                                .removePost(toDelete);
                                    }
                                    backToPostingListActivity();
                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );
        return true;
    }

    private void backToPostingListActivity() {
        Intent intent = new Intent(PostingDetailActivity.this, PostingListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.finish();
        this.startActivity(intent);
    }
}
