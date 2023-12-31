package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Price(
    @SerializedName("discountedPrice")
    @Expose
    var discountedPrice: Double,
    @SerializedName("mrp")
    @Expose
    var mrp: Double
)