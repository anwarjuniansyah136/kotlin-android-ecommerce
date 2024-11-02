package com.example.ecommerce.api.model

data class KurirResponse (
    val success : Boolean,
    val message : String,
    val data : List<Kurir>
)