package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.CartResponse
import com.example.ecommerce.api.model.PostResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CartService {
    @GET("/cart/get-all")
    fun getAll(@Header("Authorization") token : String)
    :Call<CartResponse>

    @POST("/cart/post")
    fun postCart(@Header("Authorization") token : String,
                 @Query("productId") param : String)
    :Call<PostResponse>

    @DELETE("/cart/delete")
    fun deleteCart(@Header("Authorization")token: String,
                   @Query("id")param: String)
    :Call<PostResponse>

}