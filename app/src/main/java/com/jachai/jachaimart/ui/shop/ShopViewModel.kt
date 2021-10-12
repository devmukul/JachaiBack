package com.jachai.jachaimart.ui.shop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel  (application: Application) : AndroidViewModel(application) {
    private var shopDetailsCall: Call<ShopDetailsResponse>? = null
    private val foodService = RetrofitConfig.foodService
    var successResponseLiveData = MutableLiveData<ShopDetailsResponse?>()

    fun getDriverDocStatus(shopId: String) {
        if (shopDetailsCall != null) {
            return
        } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
            return
        }

        shopDetailsCall = foodService.getShopDetails(shopId)

        shopDetailsCall?.enqueue(object : Callback<ShopDetailsResponse> {
            override fun onResponse(call: Call<ShopDetailsResponse>?, response: Response<ShopDetailsResponse>?) {
                shopDetailsCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> successResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ShopDetailsResponse>?, t: Throwable?) {
            }
        })
    }
}