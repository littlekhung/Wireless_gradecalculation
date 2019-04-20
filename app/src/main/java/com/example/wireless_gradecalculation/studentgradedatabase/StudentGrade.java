package com.example.wireless_gradecalculation.studentgradedatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(tableName = "studentgrade",
        primaryKeys = {"UID", "CID","Year","Semester"})
public class StudentGrade {
    @NonNull
    public String UID;
    @NonNull
    public String CID;
    @NonNull
    @ColumnInfo(name = "Year")
    public int year;
    @NonNull
    @ColumnInfo(name = "Semester")
    public int semester;

    @ColumnInfo(name = "Grade")
    public String grade;

    public StudentGrade(String UID, String CID, String grade,int year,int semester) {
        this.UID = UID;
        this.CID = CID;
        this.grade = grade;
        this.year=year;
        this.semester=semester;
    }
}


