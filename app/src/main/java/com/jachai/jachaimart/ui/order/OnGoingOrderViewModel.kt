package com.jachai.jachaimart.ui.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.PaymentRequestResponse
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.request.ProductsItem
import com.jachai.jachaimart.model.request.ShippingLocation
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.address.Address
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnGoingOrderViewModel(application: Application) : BaseViewModel(application) {





}