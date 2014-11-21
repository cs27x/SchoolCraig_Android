package com.cs278.schoolcraig.api;

import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.User;
import com.cs278.schoolcraig.mgmt.MyCookieManager;
import com.cs278.schoolcraig.mgmt.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public class RestClient {

    private static SchoolCraigAPI REST_CLIENT;
    private static String ROOT = "https://school-craig.herokuapp.com";
    private static String cookieKey = "rack.session";
    private static String cookieValue = null;

    static {
        setupRestClient();
    }

    private RestClient() {}

    public static SchoolCraigAPI get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        OkHttpClient client = new OkHttpClient();
        MyCookieManager manager = new MyCookieManager();
        client.setCookieHandler(manager);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(ROOT)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        builder.setConverter(new GsonConverter(gson));

        cookieValue = Preferences.getInstance().getSavedValue(cookieKey);

        if(cookieValue != null){
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Cookie", cookieKey + "=" + cookieValue);
                }
            });
        }

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(SchoolCraigAPI.class);

    }
}
