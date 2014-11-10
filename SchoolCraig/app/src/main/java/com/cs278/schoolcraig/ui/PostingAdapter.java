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

import com.cs278.schoolcraig.R;
import com.cs278.schoolcraig.UserManagement;
import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.Posting;
import com.cs278.schoolcraig.data.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.Callable;

public class PostingAdapter extends BaseAdapter implements Filterable {

	private String TAG = getClass().getSimpleName();

	private static PostingAdapter instance = null;
	private LayoutInflater mInflate;
	private Context mContext;
	private ArrayList<Posting> mData;
	private PostingFilter postingFilter;
	ArrayList<Posting> filteredPostings;

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
		this.mData = new ArrayList<Posting>();
		getFilter();
		initializePossibleCategories();
        loadDataFromBackendUsingAPI(true);
	}

	public void initializePossibleCategories() {

        final SchoolCraigAPI api = RestClient.get();
        CallableTask.invoke(new Callable<Collection<Category>>() {
                                @Override
                                public Collection<Category> call() throws Exception {
                                    Collection<Category> categories = api.getCategories();
                                    return categories;
                                }
                            }, new TaskCallback<Collection<Category>>() {

                                @Override
                                public void success(Collection<Category> result) {
                                    Log.d("SUCCESS", "categories retrieved");
                                    for (Category c : result){
                                        categories.add(c.getName());
                                    }
                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );

	}

	public void loadDataFromBackendUsingAPI(boolean update_view) {
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
                                    Log.d("SUCCESS", "categories retrieved");
                                    for(Post p : posts){
                                        mData.add(new Posting(p));
                                    }
                                }

                                @Override
                                public void error(Exception e) {
                                    Log.d("ERROR", e.getMessage().toString());
                                }
                            }
        );

        //mData.add(new Posting(1, "Roommate for sale", "Trying to get rid of them. Just taking up space.", 0, "john@vanderbilt.edu", "Free", "1/1/2014 5:00 PM"));
        //mData.add(new Posting(1, "General Chemistry Textbook", "General Chemistry textbook for both Chem 102 A and B", 125.50, "sally@vanderbilt.edu", "Textbooks", "1/15/2014 6:00 AM"));
        //mData.add(new Posting(1, "Tickets to Rites of Spring", "Can't go anymore so trying to get rid of them.", 30.00, "jack@vanderbilt.edu", "Tickets", "1/30/2014 12:00 PM"));

		if(update_view)
			this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Posting getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return (long)mData.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = this.mInflate.inflate(R.layout.list_posting_layout, parent, false);
		}
		Posting curPosting = getItem(position);
		((TextView)convertView.findViewById(R.id.posting_title))
                .setText(curPosting.getTitle());
		((TextView)convertView.findViewById(R.id.posting_description))
                .setText(curPosting.getDescription());
        ((TextView)convertView.findViewById(R.id.posting_poster))
                .setText(curPosting.getPoster());
		((TextView)convertView.findViewById(R.id.posting_category))
                .setText(curPosting.getCategory());
		((TextView)convertView.findViewById(R.id.posting_date_time))
                .setText(curPosting.getCreationDateString());
        ((TextView) convertView.findViewById(R.id.posting_price))
                .setText("$" + curPosting.getPriceString());
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
				filteredPostings = new ArrayList<Posting>();
				String constr_str = constraint.toString();
				String[] filterTypeAndValue = constr_str.split("\\|");
				String filterType = filterTypeAndValue[0];
				String filterValue =  filterTypeAndValue[1];
				
				if(filterType.equals("Category")) {
					for(Posting posting : mData) {
						if(posting.getCategory().equals(filterValue))
							filteredPostings.add(posting);
					}
				} else if (filterType.equals("Price Range")) {
                    String[] rangeStrings = filterValue.split("-");
                    double minRange = Double.parseDouble(rangeStrings[0]);
                    double maxRange = Double.parseDouble(rangeStrings[1]);
                    for(Posting posting : mData) {
                        double postingPrice = posting.getPrice();
                        if(minRange <= postingPrice && postingPrice <= maxRange)
                            filteredPostings.add(posting);
                    }
                } else if (filterType.equals("By Date")) {
					Calendar dateToCompare = Calendar.getInstance();
					dateToCompare.setTime(Posting.getDateFromString(filterValue));
					for(Posting posting : mData) {
                        Calendar postingCal = posting.getCreationDateCalendar();
						if(postingCal.get(Calendar.YEAR) == dateToCompare.get(Calendar.YEAR)
								&& postingCal.get(Calendar.DAY_OF_YEAR) == dateToCompare.get(Calendar.DAY_OF_YEAR)) {
							filteredPostings.add(posting);
						}
					}
				} else if (filterType.equals("Reset Filters")) {
                    loadDataFromBackendUsingAPI(false);
					filteredPostings = new ArrayList<Posting>(mData);
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
			mData = (ArrayList<Posting>) results.values;
			notifyDataSetChanged();
		}
	}
}
