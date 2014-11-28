package com.cs278.schoolcraig.data;

/**
 * Created by violettavylegzhanina on 11/27/14.
 */
public class NewPost {

    String id;
    double cost;
    String user_id;
    String title;
    String description;
    String category_id;
    String date;

    public NewPost(){

    }

    public NewPost(String id, double cost, String user_id, String title, String description, String category_id, String date){

        this.id = id;
        this.cost = cost;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.category_id = category_id;
        this.date = date;
    }

    public NewPost(double cost, String user_id, String title, String description, String category_id, String date){

        this.cost = cost;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.category_id = category_id;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; };
    public String getUser_id() { return user_id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategoryId() { return category_id; }
    public void setCategory(String category_id) { this.category_id = category_id; }
    public String getDate() { return date; }
    public  void setDate(String date) { this.date = date; }
}
