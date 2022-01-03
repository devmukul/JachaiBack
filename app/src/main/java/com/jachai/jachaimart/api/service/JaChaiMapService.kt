package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.map.AddressDetailsResponse
import com.jachai.jachaimart.model.map.AutoCompleteBariKoiResponse
import com.jachai.jachaimart.model.order.PaymentRequestResponse
import com.jachai.jachaimart.model.request.PaymentRequest
import com.jachai.jachaimart.model.response.pay.PaymentListResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JaChaiMapService {

    @GET(ApiConstants.AUTOCOMPLETE_SEARCH_REQUEST_BASE)
    fun mapSearchRequest(@Query("key") key: String): Call<AutoCompleteBariKoiResponse>

    @GET(ApiConstants.ADDRESS_REQUEST_BASE)
    fun addressSearchRequest(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double): Call<AddressDetailsResponse>

}