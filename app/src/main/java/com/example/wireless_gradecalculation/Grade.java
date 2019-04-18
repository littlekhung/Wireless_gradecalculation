package com.example.wireless_gradecalculation;

public class Grade {
    String courseName;
    int credit;
    String grade;
    Grade(String courseName,int credit,String grade){
        this.courseName=courseName;
        this.credit = credit;
        this.grade = grade;
    }
    Grade(){
        courseName="";
        grade="N";
        credit=0;
    }
}
