package com.jachai.jachaimart.model.order.history


import com.google.gson.annotations.SerializedName

data class ShopLocation(
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("longitude")
    var longitude: Double
)