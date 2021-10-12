package com.jachai.jachaimart.model.response.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BannerResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("banners")
    val banners: List<BannersItem?>? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
) : Parcelable

@Parcelize
data class BannersItem(

    @field:SerializedName("bannerImage")
    val bannerImage: String? = null,

    @field:SerializedName("bannerPosition")
    val bannerPosition: Int? = null,

    @field:SerializedName("bannerName")
    val bannerName: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable
