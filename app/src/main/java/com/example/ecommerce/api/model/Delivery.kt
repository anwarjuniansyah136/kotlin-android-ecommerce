package com.example.ecommerce.api.model

data class Delivery (
    val id:String,
    val productId:String,
    val productName:String,
    val productPrice:Int,
    val image:String,
    val kurirName:String,
    val sum:Int
)