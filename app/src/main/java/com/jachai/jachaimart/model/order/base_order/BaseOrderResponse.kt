package com.jachai.jachaimart.model.order.base_order


import com.google.gson.annotations.SerializedName

data class BaseOrderResponse(
    @SerializedName("currentPageNumber")
    var currentPageNumber: Int = 0,
    @SerializedName("first")
    var first: Boolean = false,
    @SerializedName("last")
    var last: Boolean = false,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("numberOfElements")
    var numberOfElements: Int = 0,
    @SerializedName("orders")
    var orders: List<BaseOrder> = listOf(),
    @SerializedName("statusCode")
    var statusCode: Int = 0,
    @SerializedName("totalElements")
    var totalElements: Int = 0,
    @SerializedName("totalPages")
    var totalPages: Int = 0
)