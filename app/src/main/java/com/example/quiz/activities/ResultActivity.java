package com.example.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quiz.R;

public class ResultActivity extends AppCompatActivity {

    TextView result, questionsNumber;
    int countCorrect, countQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // Inflate item
        result = findViewById(R.id.result);
        questionsNumber = findViewById(R.id.questions_number);
        // Getting sent data from previous Activity
        countCorrect = getIntent().getIntExtra("correct_counter", -1);
        countQuestions = getIntent().getIntExtra("question_number", -1);
        // Setting data received for result view
        result.setText(String.valueOf(countCorrect));
        questionsNumber.setText(String.valueOf(countQuestions));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("correctCorrect", countCorrect);
        outState.putInt("countQuestions", countQuestions);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        countCorrect =  savedInstanceState.getInt("correctCorrect");
        countQuestions =  savedInstanceState.getInt("countQuestions");
        result.setText(String.valueOf(countCorrect));
        questionsNumber.setText(String.valueOf(countQuestions));
    }

}