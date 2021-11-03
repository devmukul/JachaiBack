package com.jachai.jachaimart.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jachai.jachaimart.model.order.OrderDetailsResponse

class OrderViewModel : ViewModel() {

    var successOrderDetailsLiveData = MutableLiveData<OrderDetailsResponse>()
    var errorDetailsLiveData = MutableLiveData<String>()


}