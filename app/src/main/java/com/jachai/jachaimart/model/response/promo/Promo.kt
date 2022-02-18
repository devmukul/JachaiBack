package com.jachai.jachaimart.model.response.promo


import com.google.gson.annotations.SerializedName

data class Promo(
    @SerializedName("endAt")
    val endAt: String? = null, // 2022-01-10T06:41:55.758+00:00
    @SerializedName("id")
    val id: String = "", // 61dbd5387059752f307ff44c
    @SerializedName("minimumAmountPurchase")
    val minimumAmountPurchase: Int? = null, // 1000
    @SerializedName("promoCode")
    val promoCode: String = "", // TEST1000
    @SerializedName("value")
    val promoValue: PromoValue = PromoValue(),
    @SerializedName("startFrom")
    val startFrom: String = "", // 2022-01-10T06:41:55.758+00:00
    @SerializedName("status")
    val status: String = "" // ACTIVE
)