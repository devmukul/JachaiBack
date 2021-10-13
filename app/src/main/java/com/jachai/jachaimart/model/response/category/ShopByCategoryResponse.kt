package com.jachai.jachaimart.model.response.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.home.ShopsItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopByCategoryResponse(

    @field:SerializedName("shops")
    val shops: List<ShopsItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
) : Parcelable


