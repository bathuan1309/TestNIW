package com.thuan.testniw.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.thuan.testniw.api.Api;

import com.thuan.testniw.api.ApiInterface;
import com.thuan.testniw.model.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleRepository {
    private ApiInterface apiInterface;

    public ExampleRepository() {
        apiInterface = Api.getClient().create(ApiInterface.class);
    }

    public LiveData<List<Example>> getExamples(int page) {
        MutableLiveData<List<Example>> data = new MutableLiveData<>();
        apiInterface.getExamples(page).enqueue(new Callback<List<Example>>() {
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Example>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
