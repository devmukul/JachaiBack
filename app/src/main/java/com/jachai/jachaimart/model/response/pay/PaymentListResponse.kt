package com.jachai.jachaimart.model.response.pay


import com.google.gson.annotations.SerializedName

data class PaymentListResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("methods")
    var methods: List<PayMethod>,
    @SerializedName("statusCode")
    var statusCode: Int
)