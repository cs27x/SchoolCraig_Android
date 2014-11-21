package com.cs278.schoolcraig.api;

import com.cs278.schoolcraig.data.Category;
import com.cs278.schoolcraig.data.Post;
import com.cs278.schoolcraig.data.User;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by violettavylegzhanina on 11/20/14.
 */
public class PostDeserializer implements JsonDeserializer<Post> {

    @Override
    public Post deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

        final JsonObject jsonObject = je.getAsJsonObject();

        final String id = jsonObject.get("id").getAsString();

        final double cost = jsonObject.get("cost").getAsDouble();

        final JsonObject userJson = jsonObject.get("user").getAsJsonObject();
        final String user_id = userJson.get("id").getAsString();
        final String fname = userJson.get("fname").getAsString();
        final String lname = userJson.get("lname").getAsString();
        final String email = userJson.get("email").getAsString();

        final String title = jsonObject.get("title").getAsString();
        final String descr = jsonObject.get("description").getAsString();


        // TODO change this when server works correctly
        String category_id = "test";
        String name = "test";
        if(jsonObject.has("category")) {
            final JsonObject categoryJson = jsonObject.get("category").getAsJsonObject();

            category_id = categoryJson.get("id").getAsString();
            name = categoryJson.get("name").getAsString();
        }

        final String date = jsonObject.get("date").getAsString();

        // Need to user setters because setting through constructor messes things up
        final User user = new User();
        user.setId(user_id);
        user.setFname(fname);
        user.setLname(lname);
        user.setEmail(email);

        final Category category = new Category();
        category.setId(category_id);
        category.setName(name);
        //final Category  category = new Category("test");

        final Post post = new Post();
        post.setId(id);
        post.setCost(cost);
        post.setUser(user);
        post.setTitle(title);
        post.setDescription(descr);
        post.setCategory(category);
        post.setDate(date);

        return post;
    }
}
