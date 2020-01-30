package com.example.quiz.network;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkingUtils {

    private static final String BASE_URL = "https://questionapi.000webhostapp.com/";
    private static final String PAGE = "page";

    private QuestionsApi questionsApi;
    private static NetworkingUtils instance;

    public static NetworkingUtils getInstance() {
        if (instance == null) {
            instance = new NetworkingUtils();
        }
        return instance;
    }

    private NetworkingUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        questionsApi = retrofit.create(QuestionsApi.class);
    }

    public QuestionsApi getQuestionsApi() {
        return questionsApi;
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put(PAGE, "main");
        return map;
    }

}
