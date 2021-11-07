package com.jachai.jachaimart.model.response.address


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("latitude")
    var latitude: Double?,
    @SerializedName("longitude")
    var longitude: Double?
)