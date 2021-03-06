package com.cs278.schoolcraig.utils;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
//import org.joda.time.DateTime;
//import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by violettavylegzhanina on 11/3/14.
 */
public class Utils {

    public static final int REGISTER = 1;
    public static final int LOGIN = 2;
    public static final String CREATE_USER = "https://school-craig.herokuapp.com/user";
    public static final String AUTH_USER = "https://school-craig.herokuapp.com/user/auth";
    public static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm aaa", Locale.US);
    public static DateFormat utcdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public static DecimalFormat df = new DecimalFormat("#0.00");

    public static String getFormattedDateStr(String utc){

        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        DateTime dateTime = formatter.parseDateTime(utc);
        return dateTime.toString("M/d/yyyy h:mm aaa");
    }

    public static Date getDateFromUTCString(String date_str) {
        Date date = null;
        try {
            date = utcdf.parse(date_str);
        } catch(ParseException e) {
            Log.d("Utils", "Failure parsing UTC date string.");
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromString(String dateTime) {
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            Log.d("Utils", "Failure parsing the date and time.");
            e.printStackTrace();
        }
        return date;
    }

}
