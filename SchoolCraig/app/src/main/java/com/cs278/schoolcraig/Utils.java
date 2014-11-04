package com.cs278.schoolcraig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by violettavylegzhanina on 11/3/14.
 */
public class Utils {

    public static final int REGISTER = 1;
    public static final int LOGIN = 2;
    public final static String CREATE_USER = "https://school-craig.herokuapp.com/user";

    public static ArrayList<NameValuePair> makeRegisterList(String fname, String lname, String email, String password) {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        return params;
    }
}
