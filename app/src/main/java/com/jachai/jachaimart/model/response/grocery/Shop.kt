package com.jachai.jachaimart.model.response.grocery


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    @SerializedName("address")
    val address: String,
    @SerializedName("banner")
    val banner: String,
    @SerializedName("contactNumber")
    val contactNumber: String,
    @SerializedName("deliveryCharge")
    val deliveryCharge: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isFreeDelivery")
    val isFreeDelivery: Boolean,
    @SerializedName("location")
    val location: Location,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("numberOfRating")
    val numberOfRating: Int,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("timeRemaining")
    val timeRemaining: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("vat")
    val vat: Double,
    @SerializedName("minimumAmountForFreeDelivery")
    val minimumAmountForFreeDelivery: Float= 0F,

    ) : Parcelable