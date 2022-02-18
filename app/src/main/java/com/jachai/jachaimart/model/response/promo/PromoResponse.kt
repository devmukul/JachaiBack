package com.jachai.jachaimart.model.response.promo


import com.google.gson.annotations.SerializedName

data class PromoResponse(
    @SerializedName("message")
    val message: String = "", // Operation Successful
    @SerializedName("promos")
    val promos: List<Promo> = listOf(),
    @SerializedName("statusCode")
    val statusCode: Int = 0 // 200
)