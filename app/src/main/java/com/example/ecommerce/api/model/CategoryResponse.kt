package com.example.ecommerce.api.model

data class CategoryResponse (
    val success:Boolean,
    val message:String,
    val data:List<Category>
)