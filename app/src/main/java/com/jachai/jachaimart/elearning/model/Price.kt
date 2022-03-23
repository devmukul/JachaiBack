package com.jachai.jachaimart.elearning.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Price(
    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String = "",
    @SerializedName("discountedPrice")
    @Expose
    var discountedPrice: Int? = null,
    @SerializedName("mrp")
    @Expose
    var mrp: Int = 0,
    @SerializedName("tp")
    @Expose
    var tp: Int = 0
)