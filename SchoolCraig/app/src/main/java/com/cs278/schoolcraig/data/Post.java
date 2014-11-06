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

    public Post(String id, double cost, String user_id, String description, String category, String date){
        this.id = id;
        this.cost = cost;
        this.user_id = user_id;
        this.description = description;
        this.category = category;
        this.date = date;
    }
}
