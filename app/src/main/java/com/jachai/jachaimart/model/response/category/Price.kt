package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("discountedPrice")
    val discountedPrice: Float,
    @SerializedName("mrp")
    val mrp: Float
)