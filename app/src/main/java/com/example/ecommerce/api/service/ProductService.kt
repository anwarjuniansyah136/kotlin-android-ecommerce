package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.LoginResponse
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.model.ProductRequest
import com.example.ecommerce.api.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProductService {
    @PUT("/product/min-stock")
    fun minStock(@Header("Authorization") token: String,
                 @Query("productId") param1 : String,
                 @Query("sum") param2 : Int)
    :Call<PostResponse>

    @GET("/product/get-all")
    fun getAll(@Header("Authorization") token: String)
    :Call<ProductResponse>

    @POST("/product/post-product")
    fun postProduct(@Header("Authorization") token: String,
                    @Body request: ProductRequest)
    :Call<PostResponse>

    @PUT("/product/put-product")
    fun updateProduct(@Header("Authorization") token: String,
                      @Body request:ProductRequest,
                      @Query("id") param : String)
    :Call<LoginResponse>

    @DELETE("/product/delete-product")
    fun deleteProduct(@Header("Authentication") token: String,
                      @Query("id") param: String)
    :Call<LoginResponse>

    @GET("/product/find-all-exception-choose")
    fun findAll(@Header("Authentication")token:String,
                @Query("productId") param:String,
                @Query("categoryName")params: String)
    :Call<ProductResponse>

    @GET("/product/new-product")
    fun getNewProduct(@Header("Authentication")token: String)
    :Call<ProductResponse>

    @PUT("/product/upload-photo")
    fun postPhoto(@Header("Authorization") token: String,
                  @Part photo: MultipartBody.Part)
    :Call<PostResponse>
}