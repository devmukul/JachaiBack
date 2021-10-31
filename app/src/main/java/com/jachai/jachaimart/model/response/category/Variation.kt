package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class Variation(
    @SerializedName("price")
    val price: Price,
    @SerializedName("productDiscount")
    val productDiscount: ProductDiscount,
    @SerializedName("quantitativeProductDiscount")
    val quantitativeProductDiscount: QuantitativeProductDiscount,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("variationId")
    val variationId: String,
    @SerializedName("variationName")
    val variationName: String
)