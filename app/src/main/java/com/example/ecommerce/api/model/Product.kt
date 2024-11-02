package com.example.ecommerce.api.model

data class Product (
    val id:String,
    val productName:String,
    val productQuantity:Int,
    val productPrice:Int,
    val category:String,
    val image:String
)