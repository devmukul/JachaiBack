package com.jachai.jachaimart.model.response.promo


import com.google.gson.annotations.SerializedName

data class PromoValue(
    @SerializedName("flat")
    val flat: Int? = null, // 42
    @SerializedName("maxLimit")
    val maxLimit: Int = 0, // 26
    @SerializedName("percentage")
    val percentage: Int? = null // 38
)