package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.notification.NotificationRegisterRequest
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.PaymentRequestResponse
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.order.history.OrderHistoryResponse
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.product.FavouriteProductResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.*

interface NotificationsService {

    @POST("register")
    fun registerDevice(@Body notificationRegisterRequest: NotificationRegisterRequest): Call<GenericResponse>

}