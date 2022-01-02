package com.jachai.jachaimart.ui.userlocation

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.map.AddressDetailsResponse
import com.jachai.jachaimart.model.map.AutoCompleteBariKoiResponse
import com.jachai.jachaimart.model.map.BariKoiPlace
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserMapsViewModel(application: Application) : AndroidViewModel(application) {
    var successUserAddressData = MutableLiveData<CurrentLocation?>()



    private val mapService = RetrofitConfig.mapService

    private var addressDetailsCall: Call<AddressDetailsResponse>? = null

    var successAddressDetailsResponseLiveData = MutableLiveData<BariKoiPlace>()



    fun updateLocationAddress(context: Context, nowLocation: CurrentLocation) {

        var address = getAddressFromLocation(context, nowLocation)
        nowLocation.address = address

        successUserAddressData.postValue(nowLocation)
    }

    fun getAddressFromLatLan(context: Context, nowLocation: CurrentLocation){
        try {
            if (addressDetailsCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            addressDetailsCall = mapService.addressSearchRequest(nowLocation.latitude, nowLocation.longitude)

            addressDetailsCall?.enqueue(object : Callback<AddressDetailsResponse>{
                override fun onResponse(
                    call: Call<AddressDetailsResponse>,
                    response: Response<AddressDetailsResponse>
                ) {
                    addressDetailsCall = null
                    if (response.body() != null) {
                        if (response.body()?.place != null) {
                            nowLocation.address = response.body()?.place?.address.toString()
                            successUserAddressData.postValue(nowLocation)
                        } else {
                            nowLocation.address = "Out of Service area"
                            successUserAddressData.postValue(nowLocation)
                        }
                    } else {
                        nowLocation.address = "Out of Service area"
                        successUserAddressData.postValue(nowLocation)
                    }

                }

                override fun onFailure(call: Call<AddressDetailsResponse>, t: Throwable) {
                    addressDetailsCall = null
                    nowLocation.address = "Out of Service area"
                    successUserAddressData.postValue(nowLocation)
                }

            })



        }catch (ex: Exception){
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
            nowLocation.address = "Out of Service area"
            successUserAddressData.postValue(nowLocation)
        }



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
