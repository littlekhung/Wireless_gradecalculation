// this is the grade class use to keep grade information which will be use
// to display user grade in ui
// when we retrieve grade from database we will convert it into this class first
// then we will display information to user
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
