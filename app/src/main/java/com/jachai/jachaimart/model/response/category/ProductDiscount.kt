package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class ProductDiscount(
    @SerializedName("flat")
    val flat: Int,
    @SerializedName("percentage")
    val percentage: Int
)