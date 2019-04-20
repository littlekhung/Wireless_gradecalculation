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
    @Query("SELECT * FROM course WHERE CID = :courseId LIMIT 1")
    Course loadById(String courseId);
    @Query("SELECT * FROM course WHERE Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadAllByTimeAndDegType(int year, int semester, String degType);
    @Query("SELECT * FROM course WHERE CName NOT IN " +
            "(SELECT course.CName FROM course INNER JOIN studentgrade ON course.CID = studentgrade.CID " +
            "WHERE studentgrade.UID = :uid) AND Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadNonEnrollCourse(String uid,int year,int semester,String degType);
    @Query("SELECT * FROM course WHERE CName IN " +
            "(SELECT course.CName FROM course INNER JOIN studentgrade ON course.CID = studentgrade.CID " +
            "WHERE studentgrade.UID = :uid AND studentgrade.Year = :year AND studentgrade.Semester = :semester ) AND Year = :year AND Semester = :semester AND DType = :degType")
    List<Course> loadEnrollCourse(String uid,int year,int semester,String degType);
    @Insert
    void insertAll(Course... courses);
    @Delete
    void delete(Course course);
}
