package com.example.wireless_gradecalculation.studentgradedatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StudentGradeDao {
    @Query("SELECT * FROM studentgrade")
    List<StudentGrade> getAll();
    @Query("SELECT * FROM studentgrade WHERE UID IN (:studentIds)")
    List<StudentGrade> loadAllByIds(int[] studentIds);
    @Query("SELECT * FROM studentgrade WHERE CID = :cid AND UID = :uid")
    List<StudentGrade> loadGradeByCID(String cid,String uid);
    @Insert
    void insertAll(StudentGrade... studentGrades);
    @Delete
    void delete(StudentGrade studentGrade);
    @Query("DELETE FROM studentgrade WHERE CID = :cid AND UID = :uid")
    void studentRemoveCourse(String cid,String uid);
    @Query("UPDATE studentgrade SET Grade = :grade WHERE CID = :cid AND UID = :uid")
    void updateGrade(String cid,String uid,char grade);
}

