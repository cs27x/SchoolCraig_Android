package com.cs278.schoolcraig.data;

/**
 * Created by violettavylegzhanina on 11/6/14.
 */
public class Category {

    private String id;
    private String name;

    public Category(){

    }

    public Category(String id, String name){
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
