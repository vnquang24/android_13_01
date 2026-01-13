package com.example.a13_01.api

import com.example.a13_01.model.Student
import retrofit2.Call
import retrofit2.http.GET

/**
 * Retrofit API service interface for student API
 */
interface ApiService {
    
    @GET("students")
    fun getStudents(): Call<List<Student>>
}
