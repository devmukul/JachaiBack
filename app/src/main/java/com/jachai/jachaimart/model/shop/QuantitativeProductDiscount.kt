package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class QuantitativeProductDiscount(
    @SerializedName("freeProduct")
    @Expose
    var freeProduct: Any,
    @SerializedName("minimumQuantity")
    @Expose
    var minimumQuantity: Int,
    @SerializedName("productDiscount")
    @Expose
    var productDiscount: ProductDiscount
)