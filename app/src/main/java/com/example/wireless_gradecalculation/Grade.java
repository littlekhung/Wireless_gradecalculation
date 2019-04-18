package com.example.wireless_gradecalculation;

public class Grade {
    String courseName;
    int credit;
    char grade;
    Grade(String courseName,int credit,char grade){
        this.courseName=courseName;
        this.credit = credit;
        this.grade = grade;
    }
    Grade(){
        courseName="";
        grade='N';
        credit=-1;
    }
}
