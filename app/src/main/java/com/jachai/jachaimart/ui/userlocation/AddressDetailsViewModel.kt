package com.jachai.jachaimart.ui.userlocation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressDetailsViewModel(application: Application) : BaseViewModel(application) {
    private val driverService = RetrofitConfig.driverService

    private var addressCall: Call<GenericResponse>? = null

    var successResponseLiveData = MutableLiveData<GenericResponse?>()

    fun saveLocationAddress(locationDetails: LocationDetails) {
        try {
            if (addressCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            addressCall = driverService.saveUserAddress(locationDetails)

            addressCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    addressCall = null
                    if (response.body()?.statusCode ?: 100 == HttpStatusCode.HTTP_OK) {
                        successResponseLiveData.postValue(response.body())
                    }else{
                        errorResponseLiveData.value = "failed"
                    }
                    JachaiLog.d(TAG, response.body().toString())
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    addressCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.e(TAG, ex.localizedMessage)

        }

    }

}