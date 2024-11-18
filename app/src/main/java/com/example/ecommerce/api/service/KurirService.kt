package com.example.ecommerce.api.service

import com.example.ecommerce.api.model.DeliveryRequest
import com.example.ecommerce.api.model.DeliveryResponse
import com.example.ecommerce.api.model.KurirResponse
import com.example.ecommerce.api.model.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KurirService {
    @GET("/sender/get")
    fun getAll(@Header("Authorization")token:String):Call<KurirResponse>

    @POST("/delivery/post")
    fun postDelivery(@Header("Authorization")token: String,@Body request:DeliveryRequest):Call<PostResponse>

    @GET("/delivery/find-all")
    fun getAllDelivery(@Header("Authorization")token: String):Call<DeliveryResponse>

    @POST("/delivery/success")
    fun postDel(@Header("Authorization")token: String,@Query("productId")param1 :String,@Query("desc") param2:String,@Query("sum") param3:Int):Call<PostResponse>

    @DELETE("/delivery/delete")
    fun delete(@Header("Authorization")token: String,@Query("id") param : String):Call<PostResponse>
}