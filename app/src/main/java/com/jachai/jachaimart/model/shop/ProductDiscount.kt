package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductDiscount(
    @SerializedName("flat")
    @Expose
    var flat: Int,
    @SerializedName("percentage")
    @Expose
    var percentage: Int
)