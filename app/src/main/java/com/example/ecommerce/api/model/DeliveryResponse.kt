package com.example.ecommerce.api.model

data class DeliveryResponse (
    val success:Boolean,
    val message:String,
    val data:List<Delivery>
)