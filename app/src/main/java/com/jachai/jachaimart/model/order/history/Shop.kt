package com.jachai.jachaimart.model.order.history


import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("address")
    var address: String,
    @SerializedName("area")
    var area: Any,
    @SerializedName("banner")
    var banner: String,
    @SerializedName("baseDeliveryCharge")
    var baseDeliveryCharge: Int,
    @SerializedName("city")
    var city: Any,
    @SerializedName("contactNumber")
    var contactNumber: String,
    @SerializedName("country")
    var country: Any,
    @SerializedName("id")
    var id: String,
    @SerializedName("isFreeDelivery")
    var isFreeDelivery: Boolean,
    @SerializedName("logo")
    var logo: String,
    @SerializedName("mobileNumber")
    var mobileNumber: Any,
    @SerializedName("name")
    var name: String,
    @SerializedName("ownerId")
    var ownerId: String,
    @SerializedName("prepareTime")
    var prepareTime: Int,
    @SerializedName("slug")
    var slug: String,
    @SerializedName("status")
    var status: Any,
    @SerializedName("type")
    var type: String
)