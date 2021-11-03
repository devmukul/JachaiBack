package com.jachai.jachaimart.model.order.history


import com.google.gson.annotations.SerializedName

data class DeliveryMan(
    @SerializedName("id")
    var id: String,
    @SerializedName("mobileNumber")
    var mobileNumber: String,
    @SerializedName("name")
    var name: String
)