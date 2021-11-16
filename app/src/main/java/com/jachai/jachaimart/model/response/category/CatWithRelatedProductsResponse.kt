package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class CatWithRelatedProductsResponse(
    @SerializedName("productCategories")
    val catWithRelatedProducts: List<CatWithRelatedProduct>,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("currentPageNumber")
    var currentPageNumber: Int,
    @SerializedName("first")
    var first: Boolean,
    @SerializedName("last")
    var last: Boolean,
    @SerializedName("numberOfElements")
    var numberOfElements: Int,
    @SerializedName("totalElements")
    var totalElements: Int,
    @SerializedName("totalPages")
    var totalPages: Int


)