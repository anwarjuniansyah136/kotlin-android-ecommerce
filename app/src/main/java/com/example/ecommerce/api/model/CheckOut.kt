package com.example.ecommerce.api.model

data class CheckOut (
    val id:String,
    val productId:String,
    val productName:String,
    val image:String,
    val productPrice:Int,
    val sum:Int
)