package com.jachai.jachaimart.model.order.details


import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("order")
    var order: Order,
    @SerializedName("statusCode")
    var statusCode: Int
)