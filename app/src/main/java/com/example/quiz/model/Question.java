package com.example.quiz.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "questions")
public class Question {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private long question_no;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("content")
    @Expose
    private List<String> content = null;
    @SerializedName("correct")
    @Expose
    private int correct;

    public Question() {
    }

    public Question(String question, List<String> content, int correct) {
        super();
        this.question = question;
        this.content = content;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public long getQuestion_no() {
        return question_no;
    }

    public void setQuestion_no(long question_no) {
        this.question_no = question_no;
    }

}