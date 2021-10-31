package com.jachai.jachaimart.model.response.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.request.FProductsItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavouriteProductResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null,

    @field:SerializedName("products")
    val products: List<FProductsItem?>? = null
) : Parcelable


