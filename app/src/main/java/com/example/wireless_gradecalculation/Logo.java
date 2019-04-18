package com.example.wireless_gradecalculation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;

public class Logo extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_logo);
        DBHelper room = new DBHelper(this);
        try {
            room.insertCourse(new Course[]{
                    new Course("ITCS000","Introduction to Tor","Bachelor",1,1,100),
                    new Course("ITCS001","Introduction to HAHAHA","Bachelor",1,1,100),
                    new Course("ITCS002","Introduction to TorTORTOR","Bachelor",1,1,100),
                    new Course("ITCS003","Introduction to EIEI","Bachelor",1,2,100),
                    new Course("ITCS004","Introduction to Introduction","Bachelor",1,2,100)});
            room.insertGrade(new StudentGrade[]{
                    new StudentGrade("A0INeOrzqIRbSqCM8Ge3UghyNns1","ITCS000",'A'),
                    new StudentGrade("A0INeOrzqIRbSqCM8Ge3UghyNns1","ITCS001",'B'),
                    new StudentGrade("A0INeOrzqIRbSqCM8Ge3UghyNns1","ITCS003",'C'),
                    new StudentGrade("A0INeOrzqIRbSqCM8Ge3UghyNns1","ITCS004",'D')
            });
        }catch (Exception e){

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent main = new Intent(Logo.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        }, 500);
    }
}
