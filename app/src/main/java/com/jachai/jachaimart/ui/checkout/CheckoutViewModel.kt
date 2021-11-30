package com.jachai.jachaimart.ui.checkout

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.request.ProductsItem
import com.jachai.jachaimart.model.request.ShippingLocation
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel(application: Application) : BaseViewModel(application) {

    val db = JachaiApplication.mDatabase.daoAccess()
    var successProductOrderListLiveData = MutableLiveData<List<ProductOrder>>()
    var successOrderLiveData = MutableLiveData<OrderResponse>()
    var errorResponseLiveData =  MutableLiveData<String>()

    var failedResponseLiveData = MutableLiveData<GenericResponse?>()

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
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            orderCall = orderService.orderRequest(orderRequest)
            JachaiLog.d(TAG, Gson().toJson(orderRequest).toString())

            orderCall?.enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    orderCall = null


                    when (response.code()) {
                        HttpStatusCode.HTTP_OK -> successOrderLiveData.postValue(response.body())
                        else -> {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            failedResponseLiveData.value =
                                CommonConstants.DEFAULT_NON_NULL_GSON.fromJson(
                                    jObjError.toString(), GenericResponse::class.java
                                ) ?: GenericResponse()
                        }
                    }


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
            JachaiApplication.mDatabase.daoAccess().getProductOrders()
        )
    }

    fun clearCreatedOrder() {
        JachaiApplication.mDatabase.daoAccess().clearOrderTable()

    }

}