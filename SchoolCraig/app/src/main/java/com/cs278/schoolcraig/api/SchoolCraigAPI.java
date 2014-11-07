package com.cs278.schoolcraig.api;

import com.cs278.schoolcraig.data.Auth;
import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.User;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.DELETE;
import retrofit.http.Path;

import java.util.Collection;

/**
 * Created by violettavylegzhanina on 11/4/14.
 */
public interface SchoolCraigAPI {

    @GET("/category/all")
    public Collection<Category> getCategories();

    @GET("/category/id/{category_id}")
    public Category getCategory(@Path("category_id") String cat_id);

    @POST("/post")
    public Void createPost(@Body Post p);

    @GET("/post/all")
    public Collection<Post> getPosts();

    @DELETE("/post/id/{postid}")
    public Void deletePost(@Path("postid") String post_id);

    @GET("/post/id/{postid}")
    public Post getPost(@Path("postid") String post_id);

    @PUT("/post/id/{postid}")
    public Void updatePost(@Path("postid") String post_id);

    @POST("/user")
    public Void createUser(@Body User u);

    @GET("/user/all")
    public Collection<User> getUsers();

    @POST("/user/auth")
    public User authUser(@Body Auth a);

    @POST("/user/deauth")
    public Void deauthUser();

    @PUT("/user/id/{userid}")
    public Void updateUser(@Path("userid") String user_id);

    @DELETE("/user/id/{userid}")
    public Void deleteUser(@Path("userid") String user_id);

    @GET("/user/id/{userid}")
    public User getUser(@Path("userid") String user_id);

}
