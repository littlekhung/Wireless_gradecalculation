package com.example.wireless_gradecalculation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.example.wireless_gradecalculation.studentgradedatabase.Course;
import com.example.wireless_gradecalculation.studentgradedatabase.DBHelper;
import com.example.wireless_gradecalculation.studentgradedatabase.StudentGrade;

public class Logo extends LocalizationActivity {
    Course[] bachelor = new Course[]{
        new Course("SCBI109","Integrated Biology","Bachelor",1,1,3),
                new Course("SHSS116","Comparative Culture","Bachelor",1,1,2),
                new Course("ITCS320","Discrete Structures","Bachelor",1,1,3),
                new Course("ITCS375","Advanced Mathematics I for Computer Science","Bachelor",1,1,3),
                new Course("ITGE101","Problem Solving Techniques","Bachelor",1,1,2),
                new Course("ITCS200","Fundamentals of Programming","Bachelor",1,1,3),
                new Course("ITCS301","Technical English I","Bachelor",1,1,2),
                new Course("SHSS103","Man and Society","Bachelor",1,2,2),
                new Course("SCCH100","Integrated Chemistry","Bachelor",1,2,3),
                new Course("ITCS211","Introduction to Digital Systems","Bachelor",1,2,3),
                new Course("ITCS161","Physical Science and Computation","Bachelor",1,2,3),
                new Course("ITCS208","Object Oriented Programming","Bachelor",1,2,3),
                new Course("ITCS302","Technical English II","Bachelor",1,2,2),
                new Course("ITCS125","Applied Statistics for Computing","Bachelor",1,2,3),
                new Course("ITGE141","Digital Photography","Bachelor",1,2,1),
                new Course("ITGE142","Digital Drawing and Painting","Bachelor",1,2,1),
                new Course("ITLG182","Reading Skills","Bachelor",2,1,2),
                new Course("ITCS306","Numerical Methods","Bachelor",2,1,3),
                new Course("ITID276","Management","Bachelor",2,1,2),
                new Course("ITCS210","Web Programming","Bachelor",2,1,3),
                new Course("ITCS222","Computer Organization and Architecture","Bachelor",2,1,3),
                new Course("ITCS321","Data Structures and Algorithm Analysis","Bachelor",2,1,3),
                new Course("ITCS159","Software Lab for Basic Scientific Problem Solving","Bachelor",2,1,1),
                new Course("ITID275","Economics","Bachelor",2,1,2),
                new Course("ITID277","Digital Marketing ","Bachelor",2,1,2),
                new Course("ITID274","Basic Accounting","Bachelor",2,1,3),
                new Course("ITLG181","Public Speaking and Presentation","Bachelor",2,2,2),
                new Course("ITCS323","Computer Data Communication","Bachelor",2,2,3),
                new Course("ITCS381","Introduction to Multimedia Systems","Bachelor",2,2,3),
                new Course("ITCS343","Principles of Operating Systems","Bachelor",2,2,3),
                new Course("ITCS411","Database Management Systems","Bachelor",2,2,3),
                new Course("ITGE301","Communication Strategies in Professional Life","Bachelor",2,2,2),
                new Course("ITCS335","Introduction to E-business Systems","Bachelor",2,2,3),
                new Course("ITID275","Economics ","Bachelor",2,2,2),
                new Course("ITID276","Management ","Bachelor",2,2,2),
                new Course("ITID277","Digital Marketing ","Bachelor",2,2,2),
                new Course("ITID274","Basic Accounting","Bachelor",2,2,3),
                new Course("ITLG281","Business Writing","Bachelor",3,1,2),
                new Course("ITCS371","Introduction to Software Engineering","Bachelor",3,1,3),
                new Course("ITCS414","Information Storage and Retrieval","Bachelor",3,1,3),
                new Course("ITCS420","Computer Networks","Bachelor",3,1,3),
                new Course("ITCS451","Artificial Intelligence","Bachelor",3,1,3),
                new Course("ITCS443","Distributed and Parallel Systems","Bachelor",3,1,3),
                new Course("ITCS361","Management Information Systems","Bachelor",3,1,3),
                new Course("ITLG282","Academic Writing","Bachelor",3,2,2),
                new Course("ITCS424","Wireless and Mobile Computing","Bachelor",3,2,3),
                new Course("ITCS461","Computer and Communication Security","Bachelor",3,2,3),
                new Course("ITCS336","Human Computer Interface","Bachelor",3,2,3),
                new Course("ITCS368","Information and Business Process Management","Bachelor",3,2,3),
                new Course("ITCS403","Introduction to Healthcare Systems","Bachelor",3,2,3),
                new Course("ITCS453","Data Warehousing and Data Mining","Bachelor",3,2,3),
                new Course("ITCS393","Database Systems Lab","Bachelor",3,2,2),
                new Course("ITCS413","Database Design","Bachelor",3,2,3),
                new Course("ITCS422","Local Area Networks","Bachelor",3,2,3),
                new Course("ITCS481","Computer Graphics","Bachelor",3,2,3),
                new Course("ITCS431","Software Design and Development","Bachelor",3,2,3),
                new Course("ITCS498","Special Topics in Computer Science","Bachelor",3,2,3),
                new Course("ITCS493","Special Topics in Computer Networks","Bachelor",3,2,3),
                new Course("ITCS392","Multimedia Systems Lab","Bachelor",3,2,2),
                new Course("ITCS486","Multimedia Data Technologies","Bachelor",3,2,3),
                new Course("ITCS367","IT Infrastructure Management","Bachelor",3,2,3),
                new Course("ITCS439","E-Customer Relationship Management","Bachelor",3,2,3),
                new Course("ITCS391","Computer Network Lab","Bachelor",3,2,3),
                new Course("ITCS472","Software Metrics","Bachelor",3,2,3),
                new Course("ITCS402","Computer and Business Ethics","Bachelor",4,1,3),
                new Course("ITID277","Digital Marketing","Bachelor",4,1,2),
                new Course("ITCS405","Information Models and Healthcare Information Standards","Bachelor",4,1,3),
                new Course("ITCS495","Special Topics in Databases and Intelligent Systems","Bachelor",4,1,3),
                new Course("ITCS496","Special Topics in Multimedia Systems","Bachelor",4,1,3),
                new Course("ITCS498","Special Topics in Computer Science","Bachelor",4,1,3),
                new Course("ITCS425","Algorithms","Bachelor",4,1,3),
                new Course("ITCS476","Digital Image Processing","Bachelor",4,1,3),
                new Course("ITCS484","Computer Animation","Bachelor",4,1,3),
                new Course("ITCS428","Network Programming","Bachelor",4,1,3),
                new Course("ITGE301","Communication Strategies in Professional Life","Bachelor",4,1,2),
                new Course("ITCS365","Information Systems Analysis and Design","Bachelor",4,1,3),
                new Course("ITCS379","Practical Software Engineering","Bachelor",4,1,3),
                new Course("ITCS447","Embedded Systems and Internet of Things","Bachelor",4,1,3),
                new Course("ITCS473","Software Quality Assurance and Testing","Bachelor",4,1,3),
                new Course("ITCS465","Network Management","Bachelor",4,1,3),
                new Course("ITCS457","Decision Support and Business Intelligent Systems","Bachelor",4,1,3),
                new Course("ITCS363","Information Systems in Organization","Bachelor",4,1,3),
                new Course("ITCS433","Production, Supply Chain and Logistics Management","Bachelor",4,1,3),
                new Course("ITLG282","Academic Writing","Bachelor",4,1,2),
                new Course("ITCS499","Senior Project","Bachelor",4,2,6),
                new Course("ITLG282","Academic Writing","Bachelor",4,2,2)
    };
    Course[] master = new Course[]{new Course("ITCY511","Computer and Network Security","Master",1,1,3),
            new Course("ITCY541","Digital Forensics Technologies and Techniques","Master",1,1,3),
            new Course("ITCY513","Cyber Ethics and Law","Master",1,1,2),
            new Course("ITCY512","Information Security Management","Master",1,1,3),
            new Course("ITCY515","Research Methodology and Seminar in Cybersecurity and Information Assurance","Master",1,1,1),
            new Course("ITCY531","System Hardening and Penetration Testing","Master",1,2,3),
            new Course("ITCY571","Information Assurance and Risk Analysis","Master",1,2,3),
            new Course("ITCY698_","Thesis(3 Credit)","Master",1,2,3),
            new Course("ITCY514","Fraud Analysis and Detection","Master",1,2,3),
            new Course("ITCY551","Application of Cryptography","Master",1,2,3),
            new Course("ITCY534","Reverse Engineering and Vulnerability Analysis","Master",1,2,3),
            new Course("ITCY552","Authentication Technology Management","Master",1,2,3),
            new Course("ITCY543","Network Forensics","Master",1,2,3),
            new Course("ITCY553","Secure Software Design","Master",1,2,3),
            new Course("ITCY544","Mobile Security","Master",1,2,3),
            new Course("ITCY572","Information and Social Networks Security","Master",1,2,3),
            new Course("ITCY545","Cloud Security","Master",1,2,3),
            new Course("ITCY573","E-Services Security Management","Master",1,2,3),
            new Course("ITCY561","Ethical Hacking","Master",1,2,3),
            new Course("ITCY581","Incident Response Management","Master",1,2,3),
            new Course("ITCY562","Intrusion Detection and Prevention","Master",1,2,3),
            new Course("ITCY592","Special Topics in Information Assurance","Master",1,2,3),
            new Course("ITCY591","Special Topics in Cyber Security and Forensics","Master",1,2,3),
            new Course("ITCY698","Thesis(9 Credit)","Master",1,3,9),
            new Course("ITCY514","Fraud Analysis and Detection","Master",1,3,3),
            new Course("ITCY551","Application of Cryptography","Master",1,3,3),
            new Course("ITCY534","Reverse Engineering and Vulnerability Analysis","Master",1,3,3),
            new Course("ITCY552","Authentication Technology Management","Master",1,3,3),
            new Course("ITCY543","Network Forensics","Master",1,3,3),
            new Course("ITCY553","Secure Software Design","Master",1,3,3),
            new Course("ITCY544","Mobile Security","Master",1,3,3),
            new Course("ITCY572","Information and Social Networks Security","Master",1,3,3),
            new Course("ITCY545","Cloud Security","Master",1,3,3),
            new Course("ITCY573","E-Services Security Management","Master",1,3,3),
            new Course("ITCY561","Ethical Hacking","Master",1,3,3),
            new Course("ITCY581","Incident Response Management","Master",1,3,3),
            new Course("ITCY562","Intrusion Detection and Prevention","Master",1,3,3),
            new Course("ITCY592","Special Topics in Information Assurance","Master",1,3,3),
            new Course("ITCY591","Special Topics in Cyber Security and Forensics","Master",1,3,3),
            new Course("ITCY698 ","Research Project","Master",1,3,6)
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_logo);
        DBHelper room = new DBHelper(this);
        try {
                room.insertCourse(bachelor);
                room.insertCourse(master);
        }catch (Exception e){

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent main = new Intent(Logo.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        }, 1000);

    }
}
