package com.jachai.jachaimart.model.response.address


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("fullAddress")
    var fullAddress: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("location")
    var location: Location,
    @SerializedName("name")
    var name: String = "Others",
    @SerializedName("placeId")
    var placeId: String,
    @SerializedName("primaryAddress")
    var primaryAddress: String = "",
    @SerializedName("secondaryAddress")
    var secondaryAddress: String = "",

    var isSelected : Boolean= false
)