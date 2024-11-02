package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.ProductRequest
import com.example.ecommerce.api.model.ProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProductService {
    @GET("/product/get-all")
    fun getAll(@Header("Authorization") token: String) : Call<ProductResponse>
    @POST("/product/post-product")
    fun postProduct(@Header("Authorization") token: String,@Body request: ProductRequest) : Call<LoginResponse>
    @PUT("/product/put-product")
    fun updateProduct(@Header("Authorization") token: String,@Body request:ProductRequest,@Query("id") param : String): Call<LoginResponse>
    @DELETE("/product/delete-product")
    fun deleteProduct(@Header("Authentication") token: String,@Query("id") param: String):Call<LoginResponse>
}