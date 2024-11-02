package com.example.ecommerce.api.model

data class ProductRequest (
    val productName : String,
    val productPrice : Int,
    val productQuantity : Int,
    val category : String
)