package com.example.ecommerce.api.model

data class SupplyProduct (
    val id:String,
    val productName:String,
    val productPrice:Int,
    val productQuantity:Int,
    val category:String,
    val companyName:String,
    val date:String
)