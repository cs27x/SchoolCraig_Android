package com.cs278.schoolcraig.data;

/**
 * Created by violettavylegzhanina on 11/6/14.
 */
public class Auth {

    private String email;
    private String password;

    public Auth(){

    }

    public Auth(String e, String p){
        email = e;
        password = p;
    }

    public String getEmail() { return email; }
    public void setEmail() { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword() { this.password = password; }
}
