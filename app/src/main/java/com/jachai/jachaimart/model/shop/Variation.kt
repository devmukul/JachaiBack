package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Variation(
    @SerializedName("price")
    @Expose
    var price: Price,
    @SerializedName("productDiscount")
    @Expose
    var productDiscount: ProductDiscount,
    @SerializedName("quantitativeProductDiscount")
    @Expose
    var quantitativeProductDiscount: QuantitativeProductDiscount,
    @SerializedName("stock")
    @Expose
    var stock: Int,
    @SerializedName("variationId")
    @Expose
    var variationId: String,
    @SerializedName("variationName")
    @Expose
    var variationName: String
)