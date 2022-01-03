package com.jachai.jachaimart.ui.userlocation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachaimart.model.map.BariKoiPlace
import com.jachai.jachaimart.ui.base.BaseViewModel

class UserMapsViewModel(application: Application) :
    BaseViewModel(application) {

    var successAddressDetailsResponseLiveData = MutableLiveData<BariKoiPlace>()




/*

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

*/

}
