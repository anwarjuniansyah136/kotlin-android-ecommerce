package com.example.ecommerce.api.model

data class SupplyProductResponse (
    val success:Boolean,
    val message:String,
    val data:List<SupplyProduct>
)