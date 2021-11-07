package com.jachai.jachaimart.model.order.details


import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("id")
    var id: String,
    @SerializedName("mobileNumber")
    var mobileNumber: String,
    @SerializedName("name")
    var name: String
)