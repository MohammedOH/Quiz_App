package com.example.quiz.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quiz.converters.ContentListTypeConverter;
import com.example.quiz.model.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
@TypeConverters({ContentListTypeConverter.class})
public abstract class QuestionDatabase extends RoomDatabase {

    private static QuestionDatabase sInstance;
    private static final String DB_NAME = "questions_db";

    public static QuestionDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    QuestionDatabase.class,
                    DB_NAME
            ).allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public abstract QuestionDao getQuestionDao();

}
