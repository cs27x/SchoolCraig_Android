package com.cs278.schoolcraig;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class FilterPostingListActivity extends Activity {
	private String TAG = getClass().getSimpleName();
	
	private PostingAdapter adapter;
	private Spinner filterOptions;
	private Spinner filterCategoryOption;
    private EditText minPriceRange;
    private EditText maxPriceRange;
	private DatePicker filterPostingDate;
	private LinearLayout categoryOptionsView;
    private LinearLayout priceRangeOptionsView;
	private LinearLayout dateTimeOptionsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_posting_list);
		adapter = PostingAdapter.getInstance(this);
		filterOptions = (Spinner)this.findViewById(R.id.filter_option);
		addItemSelectedListenerOnFilterOptions();
		filterCategoryOption = (Spinner)this.findViewById(R.id.filter_posting_category);
        minPriceRange = (EditText)this.findViewById(R.id.min_price_range);
        maxPriceRange = (EditText)this.findViewById(R.id.max_price_range);
		filterPostingDate = (DatePicker)this.findViewById(R.id.filter_posting_date);
		categoryOptionsView = (LinearLayout)this.findViewById(R.id.category_options);
        priceRangeOptionsView = (LinearLayout)this.findViewById(R.id.price_range_options);
		dateTimeOptionsView = (LinearLayout)this.findViewById(R.id.date_time_options);
		setupSpinnerForCategories();
	}
	
	private void addItemSelectedListenerOnFilterOptions() {
		filterOptions.setOnItemSelectedListener(
				new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						categoryOptionsView.setVisibility(View.GONE);
                        priceRangeOptionsView.setVisibility(View.GONE);
						dateTimeOptionsView.setVisibility(View.GONE);
						String selectedFilter = filterOptions.getSelectedItem().toString();
						if (selectedFilter.equals("Category")) {
							categoryOptionsView.setVisibility(View.VISIBLE);
						} else if (selectedFilter.equals("Price Range")) {
                            priceRangeOptionsView.setVisibility(View.VISIBLE);
                        } else if (selectedFilter.equals("By Date")) {
							dateTimeOptionsView.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						Log.d(this.getClass().getSimpleName(), "No item selected for filtering.");
					}
				});
	}
	
	private void setupSpinnerForCategories() {
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapter.categories);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterCategoryOption.setAdapter(categoryAdapter);
	}
	
	public void filterEventsDoneButtonClick(View view) {
		String filter = filterOptions.getSelectedItem().toString() + "|";
		if (filter.equals("Category|")) {
			filter += filterCategoryOption.getSelectedItem().toString();
		} else if (filter.equals("Price Range|")){
            if(Double.parseDouble(minPriceRange.getText().toString())
                    > Double.parseDouble(maxPriceRange.getText().toString())) {
                Toast.makeText(this, "Invalid Price Range", Toast.LENGTH_LONG).show();
                return;
            } else {
                filter += minPriceRange.getText() + "-" + maxPriceRange.getText();
            }
        } else if (filter.equals("By Date|")) {
			Calendar filterDateTime = Calendar.getInstance();
			filterDateTime.set(filterPostingDate.getYear(), filterPostingDate.getMonth(),
                    filterPostingDate.getDayOfMonth());
			filter += Posting.sdf.format(filterDateTime.getTime());
		} else {
			filter += "NULL";
		}
		adapter.getFilter().filter(filter);
		finish();
	}
}