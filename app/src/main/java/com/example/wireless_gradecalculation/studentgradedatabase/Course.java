package com.example.wireless_gradecalculation.studentgradedatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "course")
public class Course {
    @NonNull
    @PrimaryKey
    public String CID;
    @ColumnInfo(name = "CName")
    public String courseName;
    @ColumnInfo(name = "DType")
    public String degreeType;
    @ColumnInfo(name = "Year")
    public int year;
    @ColumnInfo(name = "Semester")
    public int semester;
    @ColumnInfo(name = "Credit")
    public int credit;

    public Course(String CID, String courseName, String degreeType, int year, int semester, int credit) {
        this.CID = CID;
        this.courseName = courseName;
        this.degreeType = degreeType;
        this.year = year;
        this.semester = semester;
        this.credit = credit;
    }

}

