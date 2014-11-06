package com.cs278.schoolcraig.api;

import com.cs278.schoolcraig.data.Auth;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.User;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.GET;
import java.util.Collection;

/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public interface SchoolCraigAPI {

    @POST("/user")
    public Void createUser(@Body User u);

    @POST("/user/auth")
    public User authUser(@Body Auth a);

    @GET("/post/all")
    public Collection<Post> getPostings();
}
