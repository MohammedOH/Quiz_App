package com.example.quiz.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quiz.model.Question;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM questions LIMIT 1")
    LiveData<Question> getQuestions();

    @Query("SELECT * FROM questions WHERE question_no = :no")
    Question getQuestionByNo(int no);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addQuestion(Question question);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateQuestion(Question question);

    @Delete
    int deleteQuestion(Question question);

}
