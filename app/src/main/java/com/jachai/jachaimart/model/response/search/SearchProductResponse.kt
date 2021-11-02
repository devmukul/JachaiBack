package com.jachai.jachaimart.model.response.search


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.model.response.category.Product

data class SearchProductResponse(
    @SerializedName("currentPageNumber")
    @Expose
    var currentPageNumber: Int,
    @SerializedName("first")
    @Expose
    var first: Boolean,
    @SerializedName("last")
    @Expose
    var last: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("numberOfElements")
    @Expose
    var numberOfElements: Int,
    @SerializedName("products")
    @Expose
    var products: List<Product>,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int,
    @SerializedName("totalElements")
    @Expose
    var totalElements: Int,
    @SerializedName("totalPages")
    @Expose
    var totalPages: Int
)