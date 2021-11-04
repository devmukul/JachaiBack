package com.jachai.jachaimart.ui.base


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.address.AddressResponse
import com.jachai.jachaimart.ui.groceries.GroceriesShopViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = BaseViewModel::class.java
    }
    private val driverService = RetrofitConfig.driverService

    private var addressCall: Call<AddressResponse>? = null


    var successAddressResponseLiveData = MutableLiveData<AddressResponse?>()
    var errorAddressResponseLiveData = MutableLiveData<String?>()

    fun requestAllAddress() {
        try {
            if (addressCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            addressCall = driverService.getAllUserAddress()

            addressCall?.enqueue(object : Callback<AddressResponse> {
                override fun onResponse(
                    call: Call<AddressResponse>,
                    response: Response<AddressResponse>
                ) {
                    addressCall = null
                    if(response.isSuccessful){
                        successAddressResponseLiveData.value = response.body()
                    }
                    JachaiLog.d(GroceriesShopViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                    addressCall = null
                    errorAddressResponseLiveData.value = "failed"
                    JachaiLog.d(GroceriesShopViewModel.TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(GroceriesShopViewModel.TAG, ex.message!!)
        }
    }


}