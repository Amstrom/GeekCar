package com.example.sahandilshan.geekcartest;

public class Questions {
    private String questions;
    private String ans1,ans2,ans3,ans4;
    private int ans;

    public Questions(String questions, String ans1, String ans2, String ans3, String ans4,int ans) {
        this.questions = questions;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.ans=ans;
    }

    public String getQuestions() {
        return questions;
    }

    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public int getAns() {
        return ans;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "questions='" + questions + '\'' +
                ", ans1='" + ans1 + '\'' +
                ", ans2='" + ans2 + '\'' +
                ", ans3='" + ans3 + '\'' +
                ", ans4='" + ans4 + '\'' +
                ", ans=" + ans +
                '}';
    }
}
