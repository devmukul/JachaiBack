package com.jachai.jachaimart.model.order.multi_order


import com.google.gson.annotations.SerializedName

data class MultiOrderResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("order")
    var order: List<SubOrder> = listOf(),
    @SerializedName("statusCode")
    var statusCode: Int = 0
)