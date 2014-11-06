package com.cs278.schoolcraig.api;

/**
 * Created by violettavylegzhanina on 11/5/14.
 */
public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
