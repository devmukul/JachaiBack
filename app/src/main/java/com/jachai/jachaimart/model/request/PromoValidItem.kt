package com.jachai.jachaimart.model.request


import com.google.gson.annotations.SerializedName

data class PromoValidItem(
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("promoCode")
    val promoCode: String? = null,
    @SerializedName("shopId")
    val shopId: String? = null
)