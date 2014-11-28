package com.cs278.schoolcraig.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cs278.schoolcraig.mgmt.Preferences;
import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

public class PostingAdapter extends BaseAdapter implements Filterable {

	private String TAG = getClass().getSimpleName();

	private static PostingAdapter instance = null;
	private LayoutInflater mInflate;
	private Context mContext;
    private ArrayList<Post> mData;
	private PostingFilter postingFilter;
    ArrayList<Post> filteredPostings;

	public ArrayList<String> categories = new ArrayList<String>();
	
	public static PostingAdapter getInstance(Context context) {
		if (instance == null)
			instance = new PostingAdapter(context);
		return instance;
	}
	
	protected PostingAdapter(Context context) {
		this.mContext = context;
		mInflate = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Preferences.getInstance().Initialize(context);
        this.mData = new ArrayList<Post>();
	    getFilter();
		initializePossibleCategories();
        //loadDataFromBackendUsingAPI(true);
	}

	public void initializePossibleCategories() {

        mData.clear();

        final SchoolCraigAPI api = RestClient.get();
        CallableTask.invoke(new Callable<Collection<Category>>() {
                                @Override
                                public Collection<Category> call() throws Exception {
                                    return api.getCategories();
                                }
                            }, new TaskCallback<Collection<Category>>() {

                                @Override
                                public void success(Collection<Category> result) {
                                    Log.d("SUCCESS", "categories retrieved");

                                    if(result != null)
                                        Log.d("CAT SIZE", result.size()+"");

                                    for (Category c : result){
                                        Log.d("CATEGORY", c.getName());
                                        categories.add(c.getName());
                                        Preferences.getInstance().writePreference(c.getName(), c.getId());
                                    }
                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );

	}

	public void loadDataFromBackendUsingAPI(final boolean update_view) {
		mData.clear();

        final SchoolCraigAPI api = RestClient.get();
        CallableTask.invoke(new Callable<Collection<Post>>() {
                                @Override
                                public Collection<Post> call() throws Exception {
                                    Collection<Post> posts = api.getPosts();
                                    return posts;
                                }
                            }, new TaskCallback<Collection<Post>>() {

                                @Override
                                public void success(Collection<Post> posts) {
                                    Log.d("SUCCESS", "posts retrieved");
                                    for(Post p : posts){
                                        mData.add(p);
                                    }
                                    if(update_view)
                                        PostingAdapter.this.notifyDataSetChanged();

                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );
	}

    public void addPost(Post newPost){
        this.mData.add(newPost);
        this.notifyDataSetChanged();
    }
	
	@Override
	public int getCount() {
		return mData.size();
	}

    @Override
    public Post getItem(int position) {
    return mData.get(position);
}

    @Override
    public long getItemId(int position) {
    return 0;
}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = this.mInflate.inflate(R.layout.list_posting_layout, parent, false);
		}

        Post curPost = getItem(position);
        ((TextView)convertView.findViewById(R.id.posting_title))
                .setText(curPost.getTitle());
        ((TextView)convertView.findViewById(R.id.posting_description))
                .setText(curPost.getDescription());
        ((TextView)convertView.findViewById(R.id.posting_poster))
                .setText(curPost.getUser().getEmail());
        ((TextView)convertView.findViewById(R.id.posting_category))
                .setText(curPost.getCategory().getName());
        ((TextView)convertView.findViewById(R.id.posting_date_time))
                .setText(Utils.getFormattedDateStr(curPost.getDate()));
        ((TextView) convertView.findViewById(R.id.posting_price))
                .setText("$" + curPost.getCost());
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if(postingFilter==null)
			postingFilter = new PostingFilter();
		return postingFilter;
	}
	
	private class PostingFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			Log.d(this.getClass().getSimpleName(), "Filtering postings.");
			FilterResults result = new FilterResults();
			if(constraint.length() == 0) {
				result.values = mData;
				result.count = mData.size();
			} else {

                filteredPostings = new ArrayList<Post>();
				String constr_str = constraint.toString();
				String[] filterTypeAndValue = constr_str.split("\\|");
				String filterType = filterTypeAndValue[0];
				String filterValue =  filterTypeAndValue[1];
				
				if(filterType.equals("Category")) {

                    for(Post post : mData) {
                        if(post.getCategory().getName().equals(filterValue))
                            filteredPostings.add(post);
                    }
				} else if (filterType.equals("Price Range")) {
                    String[] rangeStrings = filterValue.split("-");
                    double minRange = Double.parseDouble(rangeStrings[0]);
                    double maxRange = Double.parseDouble(rangeStrings[1]);

                    for(Post post : mData) {
                        double postingPrice = post.getCost();
                        if(minRange <= postingPrice && postingPrice <= maxRange)
                            filteredPostings.add(post);
                    }
                } else if (filterType.equals("By Date")) {
					Calendar dateToCompare = Calendar.getInstance();
					dateToCompare.setTime(Utils.getDateFromString(filterValue));

                    for(Post post : mData) {
                        Calendar postingCal = Calendar.getInstance();
                        postingCal.setTime(Utils.getDateFromString(Utils.getFormattedDateStr(post.getDate())));

                        if(postingCal.get(Calendar.YEAR) == dateToCompare.get(Calendar.YEAR)
                                && postingCal.get(Calendar.DAY_OF_YEAR) == dateToCompare.get(Calendar.DAY_OF_YEAR)) {
                            filteredPostings.add(post);
                        }
                    }
				} else if (filterType.equals("Reset Filters")) {

                    loadDataFromBackendUsingAPI(true);
                    filteredPostings = new ArrayList<Post>(mData);
				}
				result.values = filteredPostings;
				result.count = filteredPostings.size();
			}
			return result;
		}

		// I know I'm going to have an ArrayList of events so supressing warning
		// rather than dealing with an ArrayList<?>
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
            mData.clear();
			mData = (ArrayList<Post>) results.values;
			notifyDataSetChanged();
		}
	}
}
