package com.example.wireless_gradecalculation;

public class Grade {
    String courseName;
    char grade;
    Grade(String courseName,char grade){
        this.courseName=courseName;
        this.grade = grade;
    }
    Grade(){
        courseName="";
        grade='N';
    }
}
