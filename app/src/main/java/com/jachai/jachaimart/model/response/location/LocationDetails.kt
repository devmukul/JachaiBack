package com.jachai.jachaimart.model.response.location

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationDetails : Parcelable {
    var placeId: String? = null

    var fullAddress: String? = null
    var primaryAddress: String? = null
    var secondaryAddress: String? = null
    var latitude: Double? = null
    var longitude: Double? = null


}