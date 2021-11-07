package com.jachai.jachaimart.model.order.details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Variation(
    @SerializedName("price")
    var price: Price,
    @SerializedName("productDiscount")
    var productDiscount: ProductDiscount,
    @SerializedName("quantitativeProductDiscount")
    var quantitativeProductDiscount: Double,
    @SerializedName("stock")
    var stock: Int,
    @SerializedName("variationId")
    var variationId: String,
    @SerializedName("variationName")
    var variationName: String
) : Parcelable