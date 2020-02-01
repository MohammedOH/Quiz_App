package com.example.quiz.network;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.quiz.database.QuestionDao;
import com.example.quiz.database.QuestionDatabase;
import com.example.quiz.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDataRepository {

    private static QuestionDataRepository sInstance;

    private Application application;
    private NetworkingUtils mNetworkingUtils;
    private QuestionDatabase mQuestionDatabase;
    private QuestionArrayApi mQuestionApi;
    private QuestionDao mQuestionDao;

    public static QuestionDataRepository getInstance(Application application) {
        if (sInstance == null) {
            sInstance = new QuestionDataRepository(application);
        }
        return sInstance;
    }

    private QuestionDataRepository(Application application) {
        this.application = application;
        mNetworkingUtils = NetworkingUtils.getInstance();
        mQuestionApi = mNetworkingUtils.getQuestionArrayApi();
        mQuestionDatabase = QuestionDatabase.getInstance(application);
        mQuestionDao = mQuestionDatabase.getQuestionDao();
    }

    public LiveData<Question> getQuestions() {
        updateQuestions();
        return mQuestionDao.getQuestions();
    }

    public Question getQuestion(int no) {
        return mQuestionDao.getQuestionByNo(no);
    }

    public void updateQuestions() {
        final Call<List<Question>> questionsApiCall = mNetworkingUtils.getQuestionArrayApi().getQuestions(mNetworkingUtils.getQueryMap());
        questionsApiCall.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NonNull Call<List<Question>> call, @NonNull Response<List<Question>> response) {
                Toast.makeText(application.getApplicationContext(), "DownLoading data ...", Toast.LENGTH_LONG).show();
                List<Question> questions = response.body();
                System.out.println(questions == null);
                if (questions != null && questions.size() > 0) {
                    for (Question q : questions) {
                        mQuestionDao.addQuestion(q);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Question>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(application.getApplicationContext(), "There was an Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
