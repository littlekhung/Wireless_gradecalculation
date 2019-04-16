package com.example.wireless_gradecalculation.studentgradedatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    List<Course> getAll();
    @Query("SELECT * FROM course WHERE CID IN (:courseIds)")
    List<Course> loadAllByIds(int[] courseIds);
    @Query("SELECT * FROM course WHERE CID IN (:courseId)")
    List<Course> loadById(String courseId);
    @Query("SELECT * FROM course WHERE Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadAllByTimeAndDegType(int year, int semester, String degType);
    @Query("SELECT * FROM course WHERE CID NOT IN " +
            "(SELECT course.CID FROM course INNER JOIN studentgrade ON course.CID = studentgrade.CID " +
            "WHERE studentgrade.UID = :uid) AND Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadNonEnrollCourse(String uid,int year,int semester,String degType);
    @Query("SELECT * FROM course WHERE CID IN " +
            "(SELECT course.CID FROM course INNER JOIN studentgrade ON course.CID = studentgrade.CID " +
            "WHERE studentgrade.UID = :uid) AND Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadEnrollCourse(String uid,int year,int semester,String degType);
    @Insert
    void insertAll(Course... courses);
    @Delete
    void delete(Course course);
}
