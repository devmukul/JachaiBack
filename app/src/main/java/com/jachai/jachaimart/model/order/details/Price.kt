package com.jachai.jachaimart.model.order.details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    @SerializedName("discountedPrice")
    var discountedPrice: Double,
    @SerializedName("mrp")
    var mrp: Double,
    @SerializedName("tp")
    var tp: Double
) : Parcelable