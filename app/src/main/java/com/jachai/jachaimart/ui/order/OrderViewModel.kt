package com.jachai.jachaimart.ui.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.history.Order
import com.jachai.jachaimart.model.order.history.OrderHistoryResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel(application: Application) : BaseViewModel(application) {

    var successOrderDetailsLiveData = MutableLiveData<OrderHistoryResponse>()
    var successOnGoingOrderListLiveData = MutableLiveData<List<Order>>()
    var successPreviousOrderListLiveData = MutableLiveData<List<Order>>()
    var errorDetailsLiveData = MutableLiveData<String>()

    private val orderService = RetrofitConfig.orderService
    private var orderCall: Call<OrderHistoryResponse>? = null

    fun getAllOrder() {
        try {
            if (orderCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            orderCall = orderService.getMyAllOrder(
                "2021-10-01",
                "2022-12-01",
                0,
                50

            )

            orderCall?.enqueue(object : Callback<OrderHistoryResponse> {
                override fun onResponse(
                    call: Call<OrderHistoryResponse>,
                    response: Response<OrderHistoryResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    orderCall = null
                    successOrderDetailsLiveData.postValue(response.body())
                    if (response.isSuccessful) {
                        if (response.body()?.statusCode ?: 0 == HttpStatusCode.HTTP_OK) {
                            postData(response.body())
                        }
                    }


                }

                override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                    orderCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorDetailsLiveData.postValue("Failed")

                }
            })

        } catch (ex: Exception) {
            errorDetailsLiveData.postValue("Failed")
            JachaiLog.d(BaseViewModel.TAG, ex.message!!)
        }
    }

    private fun postData(orderHistoryResponse: OrderHistoryResponse?) {
        val orders = orderHistoryResponse?.orders
        val onGoingOrder = mutableListOf<Order>()
        val completedOrder = mutableListOf<Order>()
        if (orders != null) {
            for (i in orders.indices) {
                if (orders[i].status.equals(ApiConstants.ORDER_COMPLETED)
                    ||
                    orders[i].status.equals(ApiConstants.ORDER_DELIVERED)
                ) {
                    completedOrder.add(orders[i])
                } else {
                    onGoingOrder.add(orders[i])
                }

            }
            successOnGoingOrderListLiveData.postValue(onGoingOrder)
            successPreviousOrderListLiveData.postValue(completedOrder)


        }
    }


}