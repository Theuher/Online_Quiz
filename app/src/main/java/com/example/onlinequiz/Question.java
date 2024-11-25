package com.example.onlinequiz;

public class Question {

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctAnswer;
    private int selectedAnswer;

    public String getQuestion() {return question;}
    public String getOption1() {return option1;}
    public String getOption2(){return option2;}
    public String getOption3() {return option3;}
    public String getOption4() {return option4;}
    public int getCorrectAnswer() {return correctAnswer;}
    public int getSelectedAnswer() {return selectedAnswer;}

    public void setQuestion(String question) {this.question = question;}
    public void setOption1(String option1) {this.option1 = option1;}
    public void setOption2(String option2) {this.option2 = option2;}
    public void setOption3(String option3) {this.option3 = option3;}
    public void setOption4(String option4) {this.option4 = option4;}
    public void setCorrectAnswer(int correctAnswer) {this.correctAnswer = correctAnswer;}
    public void setSelectedAnswer(int selectedAnswer) {this.selectedAnswer = selectedAnswer;}

}