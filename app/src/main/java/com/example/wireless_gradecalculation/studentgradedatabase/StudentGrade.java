package com.example.wireless_gradecalculation.studentgradedatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(tableName = "studentgrade",
        primaryKeys = {"UID", "CID"},foreignKeys = {
        @ForeignKey(entity = Course.class,
                parentColumns = "CID",
                childColumns = "CID")
    },indices = {@Index("CID")})
public class StudentGrade {
    @NonNull
    public String UID;
    @NonNull
    public String CID;

    @ColumnInfo(name = "Grade")
    public char grade;

    public StudentGrade(String UID, String CID, char grade) {
        this.UID = UID;
        this.CID = CID;
        this.grade = grade;
    }
}

