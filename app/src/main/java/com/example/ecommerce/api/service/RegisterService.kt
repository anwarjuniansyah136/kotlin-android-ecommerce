package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("/auth/register")
    fun create(@Body request: RegisterRequest) : Call<Void>
}