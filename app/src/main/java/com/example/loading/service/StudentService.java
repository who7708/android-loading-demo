package com.example.loading.service;

import com.example.loading.model.Student;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Chris
 * @version 1.0.0
 * @since 2021/02/20
 */
public interface StudentService {

    @GET("student/9527.json")
    Observable<Student> detail();

    @GET("student/{id}")
    Observable<Student> detail(@Path("id") int id);

    @GET("student/all")
    Observable<List<Student>> list();
}
