package com.jachai.jachaimart.model.map


import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.address.Location

data class BariKoiPlace(
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("address_bn")
    var addressBn: String? = null,
    @SerializedName("area")
    var area: String? = null,
    @SerializedName("area_bn")
    var areaBn: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("city_bn")
    var cityBn: String? = null,
    @SerializedName("location")
    var location: Location? = null,
    @SerializedName("placeType")
    var placeType: Any? = null,
    @SerializedName("postCode")
    var postCode: String? = null
)