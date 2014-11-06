package com.cs278.schoolcraig.api;

import retrofit.RestAdapter;

/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public class RestClient {

    private static SchoolCraigAPI REST_CLIENT;
    private static String ROOT = "https://school-craig.herokuapp.com";

    static {
        setupRestClient();
    }

    private RestClient() {}

    public static SchoolCraigAPI get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(SchoolCraigAPI.class);

    }
}
