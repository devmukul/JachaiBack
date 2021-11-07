package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.order.OrderDetailsResponse
import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.history.OrderHistoryResponse
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.product.FavouriteProductResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @POST(ApiConstants.ORDER_REQUEST_BASE)
    fun orderRequest(@Body orderRequest: OrderRequest): Call<OrderResponse>

    @GET(ApiConstants.ORDER_DETAILS_BASE)
    fun orderDetailsRequest(@Query("orderId") orderID: String): Call<OrderDetailsResponse>

    //favourite product
    @GET(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun getMyFavoriteProducts(): Call<FavouriteProductResponse>

    @POST(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun setAsMyFavouriteProduct(@Body fProductsItem: FProductsItem): Call<GenericResponse>


    @PUT(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun removeMyFavouriteProduct(): Call<GenericResponse>

    @GET(ApiConstants.ORDER_LIST_BASE)
    fun getMyAllOrder(
        @Query("startFrom") startFrom: String,
        @Query("endAt") endAt: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<OrderHistoryResponse>


}