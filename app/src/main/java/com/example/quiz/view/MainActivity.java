package com.example.quiz.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.example.quiz.network.NetworkingUtils;
import com.example.quiz.viewModel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "default";
    private ProgressBar progressBar;
    private NetworkingUtils networkingUtils;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
        progressBar = findViewById(R.id.main_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        networkingUtils = NetworkingUtils.getInstance();

//        if (!sharedPreferences.getBoolean("first", false)) {
            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.updateQuestions();
            sharedPreferences.edit().putBoolean("first", true).apply();
//        }
        progressBar.setVisibility(View.GONE);
    }

    public void startQuiz(View view) {
        if (view.getId() == R.id.main_btn_start) {
            startActivity(new Intent(this, QuizActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}
