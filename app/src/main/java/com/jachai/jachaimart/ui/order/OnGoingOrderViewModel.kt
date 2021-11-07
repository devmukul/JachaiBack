package com.jachai.jachaimart.ui.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.OrderDetailsResponse
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnGoingOrderViewModel(application: Application) : BaseViewModel(application) {

    private val orderService = RetrofitConfig.orderService
    private var orderCall: Call<OrderDetailsResponse>? = null


    var successOrderDetailsLiveData = MutableLiveData<OrderDetailsResponse>()
    var errorDetailsLiveData = MutableLiveData<String>()


    fun getOrderDetails(orderId: String) {
        try {


            if (orderCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            orderCall = orderService.orderDetailsRequest(orderId)

            orderCall?.enqueue(object : Callback<OrderDetailsResponse> {
                override fun onResponse(
                    call: Call<OrderDetailsResponse>,
                    response: Response<OrderDetailsResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    orderCall = null
                    successOrderDetailsLiveData.postValue(response.body())


                }

                override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                    orderCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorDetailsLiveData.postValue("Failed")

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }
    }

}