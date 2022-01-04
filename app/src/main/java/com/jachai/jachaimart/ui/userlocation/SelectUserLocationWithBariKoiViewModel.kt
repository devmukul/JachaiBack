package com.jachai.jachaimart.ui.userlocation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.map.AutoCompleteBariKoiResponse
import com.jachai.jachaimart.model.map.BariKoiPlace
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectUserLocationWithBariKoiViewModel(application: Application) :
    BaseViewModel(application) {

    private val mapService = RetrofitConfig.mapService

    private var autoCompleteLocationCall: Call<AutoCompleteBariKoiResponse>? = null

    var successAuthCompleteResponseLiveData = MutableLiveData<List<BariKoiPlace>>()

    fun findLocationAddress(query: String?) {
        try {
            if (autoCompleteLocationCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            if (query != null) {
                autoCompleteLocationCall = mapService.mapSearchRequest(query)

                autoCompleteLocationCall?.enqueue(object : Callback<AutoCompleteBariKoiResponse> {
                    override fun onResponse(
                        call: Call<AutoCompleteBariKoiResponse>,
                        response: Response<AutoCompleteBariKoiResponse>
                    ) {
                        autoCompleteLocationCall = null
                        if (response.body() != null) {
                            if (!response.body()?.places.isNullOrEmpty()) {
                                successAuthCompleteResponseLiveData.postValue(response.body()?.places!!)
                            } else {
                                successAuthCompleteResponseLiveData.postValue(emptyList())
                            }
                        } else {
                            successAuthCompleteResponseLiveData.postValue(emptyList())
                        }


                    }

                    override fun onFailure(call: Call<AutoCompleteBariKoiResponse>, t: Throwable) {
                        autoCompleteLocationCall = null
                        successAuthCompleteResponseLiveData.postValue(emptyList())
                    }

                })


            } else {
                successAuthCompleteResponseLiveData.postValue(emptyList())
            }

        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
            successAuthCompleteResponseLiveData.postValue(emptyList())
        }

    }
}