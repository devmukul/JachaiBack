package com.jachai.jachaimart.model.order.details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("productImage")
    var productImage: String,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("slug")
    var slug: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("variation")
    var variation: Variation
) : Parcelable