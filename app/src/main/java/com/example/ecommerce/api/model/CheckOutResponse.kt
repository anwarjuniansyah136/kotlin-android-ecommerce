package com.example.ecommerce.api.model

data class CheckOutResponse (
    val success:Boolean,
    val message:String,
    val data:List<CheckOut>
)