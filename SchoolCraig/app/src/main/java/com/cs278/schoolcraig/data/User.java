package com.cs278.schoolcraig.data;


/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public class User {

    private String id;
    private String fname;
    private String lname;
    private String email;
    private String password;

    public User(){
    }

    public User(String i, String f, String l, String e, String p){
        super();
        id = i;
        fname = f;
        lname = l;
        email = e;
        password = p;
    }

    public User(String f, String l, String e, String p){
        super();
        fname = f;
        lname = l;
        email = e;
        password = p;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}


























