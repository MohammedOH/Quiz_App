package com.example.quiz.network;

import com.example.quiz.model.Question;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface QuestionsApi {

    @GET("main.php")
    public Call<List<Question>> getQuestions(@QueryMap Map<String, String> params);

}
