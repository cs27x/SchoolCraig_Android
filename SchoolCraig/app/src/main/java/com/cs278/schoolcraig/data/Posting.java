package com.cs278.schoolcraig.data;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Posting {
	private static String log_class;

    public static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm aaa", Locale.US);
    public static DecimalFormat df = new DecimalFormat("#0.00");
    private int id;
    private String title;
    private String description;
    private double price;
    private String poster;
    private String category;
    private Calendar creationDate;

    // Date String must be in format "M/d/yyyy h:mm AM or PM"
    // "month/day/4-digit-year hour:minute AM-or-PM"
    // TODO add image next iteration
    public Posting(int id, String title, String description, double price, String poster, String category, String creationDate) {
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
        this(0, title, description, price, poster, category, creationDate);
    }

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
        // TODO Add posting to backend via API, Get id back and set id of posting
        // this.id = api.addPost(...);
    }

    public void editPostingViaAPI() {
        // TODO Edit posting in backend via APIs
    }

    public void deletePostingViaAPI() {
        // TODO Delete posting in backend via APIs
    }
}
