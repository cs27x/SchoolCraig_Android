package com.cs278.schoolcraig;

import com.google.gson.annotations.Expose;

/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public class User {

    private String fname;
    private String lname;
    @Expose
    private String email;
    @Expose
    private String password;

    public User(String f, String l, String e, String p){
        fname = f;
        lname = l;
        email = e;
        password = p;
    }

    public User(String e, String p){
        email = e;
        password = p;
    }

    public String getEmail(){ return email; }
}
