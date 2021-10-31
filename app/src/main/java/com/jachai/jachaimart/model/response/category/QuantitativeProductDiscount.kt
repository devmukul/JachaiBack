package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class QuantitativeProductDiscount(
    @SerializedName("freeProduct")
    val freeProduct: Any,
    @SerializedName("minimumQuantity")
    val minimumQuantity: Int,
    @SerializedName("productDiscount")
    val productDiscount: ProductDiscount
)