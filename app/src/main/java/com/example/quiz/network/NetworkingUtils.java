package com.example.quiz.network;

import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkingUtils {

    private static final String BASE_URL = "https://questionapi.000webhostapp.com/";
    private static final String PAGE = "page";

    private QuestionArrayApi questionArrayApi;
    private static NetworkingUtils instance;

    public static NetworkingUtils getInstance() {
        if (instance == null) {
            instance = new NetworkingUtils();
        }
        return instance;
    }

    private NetworkingUtils() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .build();
        questionArrayApi = retrofit.create(QuestionArrayApi.class);
    }

    public QuestionArrayApi getQuestionArrayApi() {
        return questionArrayApi;
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put(PAGE, "main");
        return map;
    }

}
