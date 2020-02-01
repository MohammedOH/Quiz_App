package com.example.quiz.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ContentListTypeConverter {

    @TypeConverter
    public String setContent(List<String> content) {
        Type type = new TypeToken<List<String>>(){}.getType();
        return new Gson().toJson(content, type);
    }

    @TypeConverter
    public List<String> getContent(String contentString) {
        Type type = new TypeToken<List<String>>(){}.getType();
        return new Gson().fromJson(contentString, type);
    }

}
