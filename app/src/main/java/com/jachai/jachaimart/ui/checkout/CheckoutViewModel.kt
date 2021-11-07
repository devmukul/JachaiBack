package com.jachai.jachaimart.ui.checkout

import android.app.Application
import androidx.lifecycle.MutableLiveData
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.google.gson.Gson
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.request.ProductsItem
import com.jachai.jachaimart.model.request.ShippingLocation
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel(application: Application) : BaseViewModel(application) {

    val db = JachaiFoodApplication.mDatabase.daoAccess()
    var successProductOrderListLiveData = MutableLiveData<List<ProductOrder>>()
    var successOrderLiveData = MutableLiveData<OrderResponse>()
    var errorResponseLiveData =  MutableLiveData<String>()

    private val orderService = RetrofitConfig.orderService
    private var orderCall: Call<OrderResponse>? = null


    fun placeOrder(additionalComment: String, deliveryAddress: Address) {
        var productOrderList: List<ProductOrder> = db.getProductOrders()

        var orderRequest = OrderRequest()
        val products = mutableListOf<ProductsItem>()

        for (item in productOrderList) {
            val productsItem = ProductsItem()
            productsItem.productId = item.product
            productsItem.quantity = item.quantity
            productsItem.variationId = item.variationId
            products.add(productsItem)
        }

        orderRequest.products = products
        orderRequest.orderNote = additionalComment

        orderRequest.shippingAddress = deliveryAddress.fullAddress
        orderRequest.shippingLocation =
            ShippingLocation(deliveryAddress.location.latitude, deliveryAddress.location.longitude)


        try {
            if (orderCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }
            orderCall = orderService.orderRequest(orderRequest)
            JachaiLog.d(TAG, Gson().toJson(orderRequest).toString())

            orderCall?.enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    JachaiLog.d(TAG, response.body().toString())
                    orderCall = null
                    successOrderLiveData.postValue(response.body())


                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    orderCall = null
                    JachaiLog.d(TAG, t.localizedMessage)
                    errorResponseLiveData.postValue("failed")
                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }

    fun geOrderList() {
        successProductOrderListLiveData.postValue(
            JachaiFoodApplication.mDatabase.daoAccess().getProductOrders()
        )
    }

    fun clearCreatedOrder() {
        JachaiFoodApplication.mDatabase.daoAccess().clearOrderTable()

    }

}