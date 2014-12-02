package com.cs278.schoolcraig.data;

import android.util.Log;

import com.cs278.schoolcraig.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

/**
 * Created by violettavylegzhanina on 11/6/14.
 */
public class Post {

    private String id;
    private double cost;
    private User user;
    private String title;
    private String description;
    private Category category;
    private String date;

    public Post() {

    }

    public Post(String id, double cost, User user, String title, String description, Category category, String date){
        this.id = id;
        this.cost = cost;
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public Post(double cost, User user, String title, String description, Category category, String date){

        this.cost = cost;
        this.user = user;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getCost() { return this.cost; }
    public String getCostString() { return Utils.df.format(this.cost); }
    public void setCost(double cost) { this.cost = cost; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getDate() { return date; }
    public  void setDate(String date) { this.date = date; }
}
