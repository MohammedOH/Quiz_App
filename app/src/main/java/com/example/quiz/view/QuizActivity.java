package com.example.quiz.view;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.quiz.R;
import com.example.quiz.model.Question;
import com.example.quiz.viewModel.MainViewModel;
import com.example.quiz.viewModel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    // Final values
    private static String CONFIRM, NEXT;
    // Resource Items creation
    private Button confirm;
    private TextView tv_question, tv_questionCount;
    private RadioButton option_1, option_2, option_3, option_4;
    private RadioGroup rg_answers;
    // Global variables each with specified purpose
    private int currentQuestion, correctAnswer, correctCount, qNumber;
    // String array to store questions
    private List<Question> questions;
    private int[] questionsNo;
    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        qNumber = getIntent().getIntExtra("q_number", 20);
        // Inflating needed items
        inflateItems();
        // Getting questions from database
        getQuestions();
        // Setting first question
        set_question(questions.get(0));
    }

    private void inflateItems() {
        // Inflating all items needed
        confirm = findViewById(R.id.btn_confirm);
        tv_question = findViewById(R.id.tv_question);
        tv_questionCount = findViewById(R.id.tv_question_count);
        option_1 = findViewById(R.id.rb_first_option);
        option_2 = findViewById(R.id.rb_second_option);
        option_3 = findViewById(R.id.rb_third_option);
        option_4 = findViewById(R.id.rb_fourth_option);
        rg_answers = findViewById(R.id.radioGroup);
        quizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        // Initializing global variables to
        currentQuestion = 0;
        correctCount = 0;
        // Initializing strings
        CONFIRM = getResources().getString(R.string.confirm);
        NEXT = getResources().getString(R.string.next);
        questions = new ArrayList<>();
    }

    /* Get all questions from database */
    private void getQuestions() {
        questions = new ArrayList<>();
        questionsNo = new int[qNumber];
        int i = 0;
        while (i < qNumber) {
            int rand = 1 + (int) (Math.random() * 95);
            boolean exist = false;
            for (int j = 0; j < questionsNo.length; j++) {
                if (rand == questionsNo[j]) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                continue;
            }
            questionsNo[i++] = rand;
        }
        getQuestionsFromDatabase();
    }

    private void getQuestionsFromDatabase() {
        for (int j = 0; j < questionsNo.length; j++) {
            questions.add(quizViewModel.getQuestion(questionsNo[j]));
        }
    }

    /* Set question to the views in layout */
    private void set_question(Question q) {
        // Setting data to each view
        tv_question.setText(q.getQuestion());
        option_1.setText(q.getContent().get(0));
        option_2.setText(q.getContent().get(1));
        option_3.setText(q.getContent().get(2));
        option_4.setText(q.getContent().get(3));
        tv_questionCount.setText(String.format("%d \t/\t%d", currentQuestion + 1, questions.size()));
        // Reassign the correct answer
        correctAnswer = q.getCorrect();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm) {
            // Check which button is clicked
            if (((Button) view).getText().toString().equals(CONFIRM)) {
                // Creation of int to store final answer entered by user
                int answer;
                // Store the button of the result
                RadioButton result;
                // Get which button is returned
                switch (rg_answers.getCheckedRadioButtonId()) {
                    case R.id.rb_first_option:
                        answer = 0;
                        result = option_1;
                        break;
                    case R.id.rb_second_option:
                        answer = 1;
                        result = option_2;
                        break;
                    case R.id.rb_third_option:
                        answer = 2;
                        result = option_3;
                        break;
                    case R.id.rb_fourth_option:
                        answer = 3;
                        result = option_4;
                        break;
                    default:
                        // Default values in case no button is selected
                        answer = -1;
                        result = null;
                }
                // Checking if no button is selected
                if (result == null) {
                    // No button selected
                    Toast.makeText(this, R.string.button_not_selected, Toast.LENGTH_LONG).show();
                } else {
                    // Setting buttons to non-clickable for the purpose of no change gets into the result
                    setClickable(false);
                    // Check if answer entered is the right one to do the appropriate reaction
                    ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.answer_anim);
                    if (answer == correctAnswer) {
                        result.setBackground(getResources().getDrawable(R.drawable.correct_answer));
                        correctCount++;
                    } else {
                        result.setBackground(getResources().getDrawable(R.drawable.wrong_answer));
                    }
                    animator.setTarget(result);
                    animator.start();
                    // Check if questions has finished to show finish button
                    if (currentQuestion < questions.size() - 1) {
                        confirm.setText(R.string.next);
                    } else {
                        confirm.setText(R.string.finish);
                    }
                }
            } else if (((Button) view).getText().toString().equals(NEXT)) {
                // Show confirm again
                confirm.setText(R.string.confirm);
                // Clear background of each radio button
                option_1.setBackground(null);
                option_2.setBackground(null);
                option_3.setBackground(null);
                option_4.setBackground(null);
                // Set buttons to be clicked again for the next question
                // Setting buttons to non-clickable for the purpose of no change gets into the result
                setClickable(true);
                // Clear previous answer from buttons
                rg_answers.clearCheck();
                // Setting the next question
                set_question(questions.get(++currentQuestion));
            } else {
                // Intent to start result activity
                Intent intent = new Intent(this, ResultActivity.class);
                // Storing quiz results in the intent to be sent
                intent.putExtra("correct_counter", correctCount);
                intent.putExtra("question_number", questions.size());
                // Starting result activity
                startActivity(intent);
                // Finishing QuizActivity to start app again case backspace clicked in ResultActivity
                finish();
            }
        }
    }

    private void setClickable(boolean isClickable) {
        option_1.setClickable(isClickable);
        option_2.setClickable(isClickable);
        option_3.setClickable(isClickable);
        option_4.setClickable(isClickable);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < questionsNo.length; i++) {
            outState.putInt("question_list_".concat(String.valueOf(i)), questionsNo[i]);
        }
        outState.putString("button_string", confirm.getText().toString());
        outState.putInt("current_question", currentQuestion);
        outState.putInt("correct_count", correctCount);
        boolean isConfirmed = !confirm.getText().toString().equals(CONFIRM);
        outState.putBoolean("isConfirmed", isConfirmed);
        int answer = -1;
        switch (rg_answers.getCheckedRadioButtonId()) {
            case R.id.rb_first_option:
                answer = 1;
                break;
            case R.id.rb_second_option:
                answer = 2;
                break;
            case R.id.rb_third_option:
                answer = 3;
                break;
            case R.id.rb_fourth_option:
                answer = 4;
                break;
        }
        outState.putInt("answer", answer);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            questionsNo[i] = savedInstanceState.getInt("question_list_".concat(String.valueOf(i)));
        }
        getQuestionsFromDatabase();
        confirm.setText(savedInstanceState.getString("button_string"));
        currentQuestion = savedInstanceState.getInt("current_question");
        correctCount = savedInstanceState.getInt("correct_count");
        set_question(questions.get(currentQuestion));
        int answer = savedInstanceState.getInt("answer");
        RadioButton result;
        switch (answer) {
            case 1:
                result = option_1;
                break;
            case 2:
                result = option_2;
                break;
            case 3:
                result = option_3;
                break;
            case 4:
                result = option_4;
                break;
            default:
                return;
        }
        result.setChecked(true);
        if (savedInstanceState.getBoolean("isConfirmed")) {
            if (answer == correctAnswer) {
                result.setBackground(getResources().getDrawable(R.drawable.correct_answer));
            } else {
                result.setBackground(getResources().getDrawable(R.drawable.wrong_answer));
            }
            // Setting buttons to non-clickable for the purpose of no change gets into the result
            setClickable(false);
        }
    }

}