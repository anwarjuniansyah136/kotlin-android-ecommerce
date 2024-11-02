package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.LoginRequest
import com.example.ecommerce.api.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest):Call<LoginResponse>
}