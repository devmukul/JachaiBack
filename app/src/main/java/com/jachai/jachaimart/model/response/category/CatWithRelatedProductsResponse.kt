package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class CatWithRelatedProductsResponse(
    @SerializedName("productCategories")
    val catWithRelatedProducts: List<CatWithRelatedProduct>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int
)