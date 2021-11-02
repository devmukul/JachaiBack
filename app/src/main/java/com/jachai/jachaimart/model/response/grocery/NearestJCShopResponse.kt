package com.jachai.jachaimart.model.response.grocery


import com.google.gson.annotations.SerializedName

data class NearestJCShopResponse(
    @SerializedName("currentPageNumber")
    val currentPageNumber: Int,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("shops")
    val shops: List<Shop>,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)