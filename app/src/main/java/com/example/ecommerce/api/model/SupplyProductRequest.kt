package com.example.ecommerce.api.model

data class SupplyProductRequest (
    val productName:String,
    val productPrice:Int,
    val productQuatity:Int,
    val date:String,
    val companyName:String,
    val category:String
)