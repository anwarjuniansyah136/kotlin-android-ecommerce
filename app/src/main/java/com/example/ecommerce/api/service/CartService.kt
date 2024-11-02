package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.CartResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CartService {
    @GET("/cart/get-all")
    fun getAll(@Header("Authorization") token : String):Call<CartResponse>
    @POST("/cart/post")
    fun postCart(@Header("Authorization") token : String,@Query("productId") param : String):Call<CartResponse>
}