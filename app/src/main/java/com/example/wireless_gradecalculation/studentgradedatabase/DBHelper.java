package com.example.wireless_gradecalculation.studentgradedatabase;

import android.content.Context;

import com.example.wireless_gradecalculation.Grade;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DBHelper {
    private CourseDao course;
    private StudentGradeDao grade;

    public DBHelper(Context context) {
        this.course = AppDatabase
                .getInstance(context.getApplicationContext())
                .getCourseDao();
        this.grade = AppDatabase
                .getInstance(context.getApplicationContext())
                .getStudentGradeDao();
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

    public Course getCourseByID(final String cid) throws ExecutionException, InterruptedException {

        Callable<Course> callable = new Callable<Course>() {
            @Override
            public Course call() throws Exception {
                return course.loadById(cid);
            }
        };

        Future<Course> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public List<Course> loadEnrollCourse(final String uid,final int year,final int semester,final String degType) throws ExecutionException, InterruptedException {

        Callable<List<Course>> callable = new Callable<List<Course>>() {
            @Override
            public List<Course> call() throws Exception {
        return course.loadEnrollCourse(uid,year,semester,degType);
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

    public void insertCourse(final Course[] courses) throws Exception{
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call(){
                course.insertAll(courses);
                return null;
            }
        };
        Future<Void> future = Executors.newSingleThreadExecutor().submit(callable);
        future.get();
    }

    public void insertGrade(final StudentGrade[] grades) throws Exception{
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call(){
                grade.insertAll(grades);
                return null;
            }
        };
        Future<Void> future = Executors.newSingleThreadExecutor().submit(callable);
        future.get();
    }

    public void deleteGrade(final String UID, final String CID) throws Exception{
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call(){
                grade.studentRemoveCourse(UID,CID);
                return null;
            }
        };
        Future<Void> future = Executors.newSingleThreadExecutor().submit(callable);
        future.get();
    }

}
