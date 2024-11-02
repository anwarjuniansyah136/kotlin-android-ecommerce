package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.DeliveryRequest
import com.example.ecommerce.api.model.KurirResponse
import com.example.ecommerce.api.model.PostResponse
import com.example.ecommerce.api.model.ProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface KurirService {
    @GET("/sender/get")
    fun getAll(@Header("Authorization")token:String):Call<KurirResponse>

    @POST("/delivery/post")
    fun postDelivery(@Header("Authorization")token: String,@Body request:DeliveryRequest):Call<PostResponse>

    @GET("/sender/get-by-sender")
    fun getDeliveryBySender(@Header("Authorization")token: String):Call<ProductResponse>
}