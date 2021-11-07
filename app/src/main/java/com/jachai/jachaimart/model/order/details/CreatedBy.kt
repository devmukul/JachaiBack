package com.jachai.jachaimart.model.order.details


import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("id")
    var id: String,
    @SerializedName("mobileNumber")
    var mobileNumber: String,
    @SerializedName("name")
    var name: String
)