package com.jachai.jachaimart.model.request


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class PaymentRequest(
    @SerializedName("amount")
    @Expose
    var amount: Double,
    @SerializedName("orderId")
    @Expose
    var orderId: String,
    @SerializedName("gateway")
    @Expose
    var gateway: String,
    @SerializedName("successUrl")
    @Expose
    var successUrl: String,
    @SerializedName("failUrl")
    @Expose
    var failUrl: String,
    @SerializedName("cancelUrl")
    @Expose
    var cancelUrl: String
)