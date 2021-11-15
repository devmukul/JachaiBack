package com.jachai.jachaimart.model.response.grocery


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    @SerializedName("coordinates")
    val coordinates: List<Double>,
    @SerializedName("type")
    val type: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
) : Parcelable