package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.CheckOutResponse
import com.example.ecommerce.api.model.PostResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckOutService {
    @POST("/checkout/post")
    fun postCheckOut(@Header("Authorization")token:String,@Query("productId") param : String,@Query("sum") sum : Int):Call<PostResponse>
    @GET("/checkout/find-all")
    fun getAll(@Header("Authorization")token: String):Call<CheckOutResponse>
    @DELETE("/checkout/delete")
    fun delete(@Header("Authorization")token: String,@Query("id")param: String):Call<PostResponse>
}