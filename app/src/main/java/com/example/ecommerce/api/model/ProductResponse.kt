package com.example.ecommerce.api.model

data class ProductResponse (
    val success:Boolean,
    val message:String,
    val data:List<Product>
)