package com.example.ecommerce.api.model

data class DeliveryRequest (
    val productId:String,
    val senderId:String,
    val sum:Int
)