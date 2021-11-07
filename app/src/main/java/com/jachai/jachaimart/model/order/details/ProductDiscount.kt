package com.jachai.jachaimart.model.order.details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDiscount(
    @SerializedName("flat")
    var flat: Int,
    @SerializedName("maxLimit")
    var maxLimit: Int,
    @SerializedName("percentage")
    var percentage: Int
) : Parcelable