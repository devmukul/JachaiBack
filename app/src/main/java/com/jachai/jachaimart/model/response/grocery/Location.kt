package com.jachai.jachaimart.model.response.grocery


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("coordinates")
    val coordinates: List<Double>,
    @SerializedName("type")
    val type: String,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)