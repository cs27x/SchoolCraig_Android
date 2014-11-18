package com.cs278.schoolcraig.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cs278.schoolcraig.api.CallableTask;
import com.cs278.schoolcraig.api.RestClient;
import com.cs278.schoolcraig.api.SchoolCraigAPI;
import com.cs278.schoolcraig.api.TaskCallback;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

public class Posting {
	private static String log_class;

    public static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm aaa", Locale.US);
    public static DecimalFormat df = new DecimalFormat("#0.00");
    private int id;
    private String user_id;
    private String title;
    private String description;
    private double price;
    private String poster;
    private String category;
    private Calendar creationDate;

    // Date String must be in format "M/d/yyyy h:mm AM or PM"
    // "month/day/4-digit-year hour:minute AM-or-PM"
    // TODO add image next iteration
    public Posting(Post p){
        this(Integer.valueOf(p.getId()), p.getUser_id(), "Add Title to getPosts?", p.getDescription(), p.getCost(), p.getUser_id(), p.getCategoryId(), p.getDate());
    }

    public Posting(int id, String user_id, String title, String description, double price, String poster, String category, String creationDate) {
    	log_class = this.getClass().getSimpleName();
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.poster = poster;
        this.category = category;
        this.creationDate = Calendar.getInstance();
        this.creationDate.setTime(getDateFromString(creationDate));
    }

    public Posting(String title, String description, double price, String poster, String category, String creationDate) {
        this(0, "anonymous", title, description, price, poster, category, creationDate);
    }

    public String getUser_id() { return this.user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return this.price; }
    public String getPriceString() { return df.format(this.price); }
    public void setPrice(double price) { this.price = price; }
    public String getPoster() { return this.poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }
    public String getCreationDateString() { return sdf.format(this.creationDate.getTime()); }
    public Calendar getCreationDateCalendar() { return this.creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate.setTime(getDateFromString(creationDate)); }

    public static Date getDateFromString(String dateTime) {
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            Log.d(log_class, "Failure parsing the date and time.");
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public String toString() {
        return title;
    }

    public void addPostingViaAPI() {
        new Add().execute(this);
    }

    public void editPostingViaAPI() {
        new Edit().execute(this);
    }

    public void deletePostingViaAPI() {
        new Delete().execute(this);
    }

    public class Delete extends AsyncTask<Posting, Void, Void> {
        @Override
        protected Void doInBackground(Posting... postings) {
            final SchoolCraigAPI api = RestClient.get();
            api.deletePost(String.valueOf(postings[0].getId()));
            return null;
        }
    }

    public class Add extends AsyncTask<Posting, Void, Void> {
        @Override
        protected Void doInBackground(Posting... postings) {
            final SchoolCraigAPI api = RestClient.get();
            api.createPost(new Post(postings[0]));
            return null;
        }
    }

    public class Edit extends AsyncTask<Posting, Void, Void> {
        @Override
        protected Void doInBackground(Posting... postings) {
            final SchoolCraigAPI api = RestClient.get();
            api.updatePost(String.valueOf(postings[0].getId()), new Post(postings[0]));
            return null;
        }
    }
}
