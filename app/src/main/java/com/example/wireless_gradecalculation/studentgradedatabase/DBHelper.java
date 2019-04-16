package com.example.wireless_gradecalculation.studentgradedatabase;

import com.example.wireless_gradecalculation.Grade;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DBHelper {
    private CourseDao course;
    private StudentGradeDao grade;

    public DBHelper(CourseDao course, StudentGradeDao grade) {
        this.course = course;
        this.grade = grade;
    }
    public List<Course> loadNonEnrollCourse(final String uid,final int year,final int semester,final String degType) throws ExecutionException, InterruptedException {

        Callable<List<Course>> callable = new Callable<List<Course>>() {
            @Override
            public List<Course> call() throws Exception {
                return course.loadNonEnrollCourse(uid,year,semester,degType);
            }
        };

        Future<List<Course>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public StudentGrade loadGradeByCID(final String cid,final String uid) throws ExecutionException, InterruptedException {

        Callable<StudentGrade> callable = new Callable<StudentGrade>() {
            @Override
            public StudentGrade call() throws Exception {
                return grade.loadGradeByCID(cid,uid);
            }
        };

        Future<StudentGrade> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }
}
