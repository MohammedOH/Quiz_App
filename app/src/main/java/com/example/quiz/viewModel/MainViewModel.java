package com.example.quiz.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quiz.model.Question;
import com.example.quiz.network.QuestionDataRepository;


public class MainViewModel extends AndroidViewModel {

    private QuestionDataRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = QuestionDataRepository.getInstance(application);
    }

    public boolean dataDownloaded() {
        return mRepository.getQuestion(45) != null;
    }

    public Question getQuestion(int no) {
        return mRepository.getQuestion(no);
    }

    public void updateQuestions(QuestionDataRepository.RetrofitResponseListener retrofitResponseListener) {
        mRepository.updateQuestions(retrofitResponseListener);
    }

}
