package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.order.OrderDetailsResponse
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderService {

    @POST(ApiConstants.ORDER_REQUEST_BASE)
    fun orderRequest(@Body orderRequest: OrderRequest): Call<OrderResponse>

    @GET(ApiConstants.ORDER_DETAILS_BASE)
    fun orderDetailsRequest(@Query("orderId")orderID: String): Call<OrderDetailsResponse>


}