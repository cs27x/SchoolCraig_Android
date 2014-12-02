package com.cs278.schoolcraig.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs278.schoolcraig.mgmt.Preferences;
import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.utils.Utils;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link PostingListActivity}
 * in two-pane mode (on tablets) or a {@link PostingDetailActivity}
 * on handsets.
 */
public class PostingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
	public static final String ARG_POSTING_INDEX = "posting_index";

    /**
     * The dummy content this fragment is presenting.
     */
//    private Posting mPosting;
    private Post mPosting;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostingDetailFragment() {
    }

    public Post getmPosting(){
        return mPosting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args.containsKey(ARG_POSTING_INDEX)) {
        	int index = args.getInt(ARG_POSTING_INDEX);
           	mPosting = PostingAdapter.getInstance(this.getActivity().getApplicationContext())
        			.getItem(index);
        }
        Preferences.getInstance().Initialize(this.getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posting_detail, container, false);

        if (mPosting != null) {
            ((TextView) rootView.findViewById(R.id.posting_detail_title))
                    .setText(mPosting.getTitle() + " - $" + mPosting.getCost() );
            ((TextView) rootView.findViewById(R.id.posting_detail_category))
                    .setText(mPosting.getCategory().getName());
            ((TextView) rootView.findViewById(R.id.event_detail_description))
                    .setText(mPosting.getDescription());
            ((TextView) rootView.findViewById(R.id.posting_detail_poster))
                    .setText(mPosting.getUser().getEmail());
            ((TextView) rootView.findViewById(R.id.posting_detail_creation_date))
                    .setText("Posted: " + Utils.getFormattedDateStr(mPosting.getDate()));
        }
        return rootView;
    }
}
