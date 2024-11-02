package com.example.ecommerce.api.model

data class LoginResponse (
    val success:Boolean,
    val message:String,
    val data:Login
)