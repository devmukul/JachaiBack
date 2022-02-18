package com.jachai.jachaimart.model.response.promo


import com.google.gson.annotations.SerializedName

data class PromoValidationResponse(

    var promoCode :String? = null, // code
    @SerializedName("discountAmount")
    val discountAmount: Double? = null, // 200
    @SerializedName("message")
    val message: String? = null, // Operation Successful
    @SerializedName("minimumAmountPurchase")
    val minimumAmountPurchase: Double? = null, // 1000
    @SerializedName("statusCode")
    val statusCode: Int? = null, // 200
    @SerializedName("value")
    val promoValue: PromoValue = PromoValue()
)