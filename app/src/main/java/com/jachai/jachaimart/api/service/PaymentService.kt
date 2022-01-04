package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.order.PaymentRequestResponse
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.pay.PaymentListResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentService {

    @POST(ApiConstants.PAYMENT_REQUEST_BASE)
    fun paymentRequest(@Body paymentRequest: PaymentRequest): Call<PaymentRequestResponse>

    @GET(ApiConstants.PAYMENT_METHOD_REQUEST_BASE)
    fun paymentMethodRequest(@Query("type") type: String = "JC_MART"): Call<PaymentListResponse>

}