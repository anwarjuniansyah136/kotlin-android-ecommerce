package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.SupplyProductRequest
import com.example.ecommerce.api.model.SupplyProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SupplyProduct {
    @GET("/supply-product/get")
    fun getAll(@Header("Authorization")token : String):Call<SupplyProductResponse>
    @POST("/supply-product/post")
    fun postSupply(@Header("Authorization")token: String,@Body request: SupplyProductRequest):Call<LoginResponse>
    @DELETE("/supply-product/delete")
    fun deleteSupply(@Header("Authorization")token: String,@Query("id") param:String):Call<LoginResponse>
    @PUT("/supply-product/update")
    fun updateSupply(@Header("Authorization")token: String,@Query("id") param: String,@Body request: SupplyProductRequest):Call<LoginResponse>
}