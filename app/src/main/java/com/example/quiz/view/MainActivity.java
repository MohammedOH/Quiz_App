package com.example.quiz.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.example.quiz.viewModel.MainViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String NAME = "default";
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private TextInputLayout qNumberInputLayout;
    private int qNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
        progressBar = findViewById(R.id.main_progress_bar);
        qNumberInputLayout = findViewById(R.id.text_input_q_num);

        findViewById(R.id.activity).setOnClickListener(this);

        if (!sharedPreferences.getBoolean("first", false)) {
            progressBar.setVisibility(View.VISIBLE);
            MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.updateQuestions();
            sharedPreferences.edit().putBoolean("first", true).apply();

        }
        progressBar.setVisibility(View.GONE);
    }

    public void startQuiz(View view) {
        String qNum = qNumberInputLayout.getEditText().getText().toString().trim();
        // Edge case: Empty fields
        if (qNum.isEmpty()) {
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_LONG).show();
            return;
        }
        qNumber = Integer.parseInt(qNum);
        if (!validQNum())
            return;
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("q_number", qNumber);
        startActivity(intent);
    }

    private boolean validQNum() {
        if (qNumber < 5 || qNumber > 45) {
            qNumberInputLayout.setError(getResources().getText(R.string.invalid_q_num));
            return false;
        }
        qNumberInputLayout.setError(null);
        return true;
    }

    // Hides keyboard when touch the screen
    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.text_input_q_num)
            hideKeyboard(this);
    }

    /* This method hides the keyboard of the activity send to it as parameter */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}