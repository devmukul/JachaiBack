package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ShopDetailsResponse(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("products")
    @Expose
    var products: List<Product>,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int
)