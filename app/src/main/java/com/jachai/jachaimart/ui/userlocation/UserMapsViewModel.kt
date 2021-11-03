package com.jachai.jachaimart.ui.userlocation

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import bd.com.evaly.ehealth.models.common.CurrentLocation
import java.util.*

class UserMapsViewModel(application: Application) : AndroidViewModel(application) {
    var successUserAddressData = MutableLiveData<CurrentLocation?>()

    fun updateLocationAddress(context: Context, nowLocation: CurrentLocation) {

        var address = getAddressFromLocation(context, nowLocation)
        nowLocation.address = address

        successUserAddressData.postValue(nowLocation)
    }

    private fun getAddressFromLocation(context: Context, location: CurrentLocation): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses.isNotEmpty()) {

                //val userCountry = addresses[0].getCountryName()
                addresses[0].getAddressLine(0)
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


}
