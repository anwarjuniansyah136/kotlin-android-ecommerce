package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.CategoryRequest
import com.example.ecommerce.api.model.CategoryResponse
import com.example.ecommerce.api.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CategoryService {
    @GET("/category/get")
    fun getAll(@Header("Authorization") token:String):Call<CategoryResponse>
    @POST("/category/post")
    fun postCategory(@Header("Authorization") token: String,@Body request:CategoryRequest):Call<LoginResponse>
    @DELETE("/category/delete")
    fun deleteProduct(@Header("Authorization") token: String,@Query("id") param : String):Call<LoginResponse>
    @PUT("/category/put")
    fun updateCategory(@Header("Authorization") token: String,@Query("id") param: String,@Body request: CategoryRequest):Call<LoginResponse>
}