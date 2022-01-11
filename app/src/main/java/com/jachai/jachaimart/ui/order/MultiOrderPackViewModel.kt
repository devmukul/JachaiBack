package com.jachai.jachaimart.ui.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.base_order.BaseOrderResponse
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.order.multi_order.BaseOrderDetailsResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MultiOrderPackViewModel(application: Application) : BaseViewModel(application) {


    private val orderService = RetrofitConfig.orderService
    private var multiOrderPackCall: Call<BaseOrderDetailsResponse>? = null

    var successMultiOrderDetailsLiveData = MutableLiveData<BaseOrderDetailsResponse>()
    var errorMultiOrderDetailsLiveData = MutableLiveData<String>()

    fun getMultiOrderDetails(orderId: String) {
        try {

            if (multiOrderPackCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            multiOrderPackCall = orderService.orderDetailsRequestV2(orderId)


            multiOrderPackCall?.enqueue(object : Callback<BaseOrderDetailsResponse> {
                override fun onResponse(
                    call: Call<BaseOrderDetailsResponse>,
                    response: Response<BaseOrderDetailsResponse>
                ) {
                    if (response.isSuccessful){
                        JachaiLog.d(TAG, response.body().toString())
                        multiOrderPackCall = null
                        successMultiOrderDetailsLiveData.postValue(response.body())
                    }else{
                        errorMultiOrderDetailsLiveData.postValue("Failed")
                    }


                }

                override fun onFailure(call: Call<BaseOrderDetailsResponse>, t: Throwable) {
                    multiOrderPackCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorMultiOrderDetailsLiveData.postValue("Failed")

                }
            })



        } catch (ex: Exception) {

        }
    }

}