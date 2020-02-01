package com.example.quiz.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.quiz.model.Question;
import com.example.quiz.network.QuestionDataRepository;

public class QuizViewModel extends AndroidViewModel {

    private QuestionDataRepository mRepository;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        mRepository = QuestionDataRepository.getInstance(application);
    }

    public Question getQuestion(int no) {
        return mRepository.getQuestion(no);
    }

}
