package com.jachai.jachaimart.model.order.history


import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(
    @SerializedName("currentPageNumber")
    var currentPageNumber: Int,
    @SerializedName("first")
    var first: Boolean,
    @SerializedName("last")
    var last: Boolean,
    @SerializedName("message")
    var message: String,
    @SerializedName("numberOfElements")
    var numberOfElements: Int,
    @SerializedName("orders")
    var orders: List<Order>,
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("totalElements")
    var totalElements: Int,
    @SerializedName("totalPages")
    var totalPages: Int
)