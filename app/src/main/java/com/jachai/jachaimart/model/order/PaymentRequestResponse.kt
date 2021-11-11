package com.jachai.jachaimart.model.order


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PaymentRequestResponse(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int,
    @SerializedName("url")
    @Expose
    var url: String
)