package com.thuan.testniw.api;

import com.thuan.testniw.model.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("users/google/repos")
    Call<List<Example>> getExamples(@Query("page") int page);
}
