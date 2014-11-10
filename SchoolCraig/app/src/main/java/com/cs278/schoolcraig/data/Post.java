package com.cs278.schoolcraig.data;

/**
 * Created by violettavylegzhanina on 11/6/14.
 */
public class Post {

    private String id;
    private double cost;
    private String user_id;
    private String description;
    private String category;
    private String date;

    public Post(){

    }

    public Post(Posting p) {
        this.id = String.valueOf(p.getId());
        this.cost = p.getPrice();
        this.user_id = p.getUser_id();
        this.description = p.getDescription();
        this.category = p.getCategory();
        this.date = p.getCreationDateString();
    }

    public Post(String id, double cost, String user_id, String description, String category, String date){
        this.id = id;
        this.cost = cost;
        this.user_id = user_id;
        this.description = description;
        this.category = category;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; };
    public String getUser_id() { return user_id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDate() { return date; }
    public  void setDate(String date) { this.date = date; }
}
