package com.example.quiz.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quiz.model.Question;
import com.example.quiz.network.QuestionDataRepository;


public class MainViewModel extends AndroidViewModel {

    private QuestionDataRepository mRepository;
    private LiveData<Question> mQuestionLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = QuestionDataRepository.getInstance(application);
        mQuestionLiveData = mRepository.getQuestions();
    }

    public LiveData<Question> getQuestions() {
        return mQuestionLiveData;
    }

    public void updateQuestions() {
        mRepository.updateQuestions();
    }

}
