package com.jachai.jachaimart.model.response.address


import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("addresses")
    var addresses: MutableList<Address>,
    @SerializedName("message")
    var message: String,
    @SerializedName("statusCode")
    var statusCode: Int
)