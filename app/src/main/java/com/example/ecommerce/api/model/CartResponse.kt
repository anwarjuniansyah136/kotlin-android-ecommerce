package com.example.ecommerce.api.model

data class CartResponse (
    val success:Boolean,
    val message:String,
    val data:List<Cart>
)