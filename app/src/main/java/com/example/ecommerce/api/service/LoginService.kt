package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.LoginRequest
import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.ProfilResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest):Call<LoginResponse>
    @GET("/auth/profil")
    fun profil(@Header("Authorization")token:String):Call<ProfilResponse>
}