package com.cs278.schoolcraig.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.Posting;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args.containsKey(ARG_POSTING_INDEX)) {
        	int index = args.getInt(ARG_POSTING_INDEX);
           	mPosting = PostingAdapter.getInstance(this.getActivity().getApplicationContext())
        			.getItem(index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posting_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mPosting != null) {
//            ((TextView) rootView.findViewById(R.id.posting_detail_title))
//                    .setText(mPosting.getTitle() + " - $" + mPosting.getPriceString() );
//            ((TextView) rootView.findViewById(R.id.posting_detail_category))
//                    .setText(mPosting.getCategory());
//            ((TextView) rootView.findViewById(R.id.event_detail_description))
//                    .setText(mPosting.getDescription());
//            ((TextView) rootView.findViewById(R.id.posting_detail_poster))
//                    .setText(mPosting.getPoster());
//            ((TextView) rootView.findViewById(R.id.posting_detail_creation_date))
//                    .setText("Posted: " + mPosting.getCreationDateString());
//        }
        if (mPosting != null) {
            ((TextView) rootView.findViewById(R.id.posting_detail_title))
                    .setText(mPosting.getTitle() + " - $" + mPosting.getCost() );
            SharedPreferences prefs = this.getActivity().getSharedPreferences(Utils.CATEGORY_SHARED_PREFS, 0);
            ((TextView) rootView.findViewById(R.id.posting_detail_category))
                    .setText(prefs.getString(mPosting.getCategoryId(), ""));
            ((TextView) rootView.findViewById(R.id.event_detail_description))
                    .setText(mPosting.getDescription());
//            ((TextView) rootView.findViewById(R.id.posting_detail_poster))
//                    .setText(mPosting.getPoster());
            ((TextView) rootView.findViewById(R.id.posting_detail_creation_date))
                    .setText("Posted: " + mPosting.getDate());
        }
        return rootView;
    }
}
