package com.jachai.jachaimart.model.response.location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.address.Location
import com.jachai.jachaimart.model.response.home.CategoriesItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationDetails : Parcelable {
    @SerializedName("placeId")
    var placeId: String? = null

    @SerializedName("fullAddress")
    var fullAddress: String? = null

    @SerializedName("primaryAddress")
    var primaryAddress: String? = null

    @SerializedName("secondaryAddress")
    var secondaryAddress: String? = null

    @SerializedName("name")
    var name: String? = null

    var latitude: Double? = null

    var longitude: Double? = null


    @SerializedName("location")
    var favLocation: Location? = null


}

