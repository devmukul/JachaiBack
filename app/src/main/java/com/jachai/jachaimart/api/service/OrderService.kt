package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.order.OrderResponse
import com.jachai.jachaimart.model.order.base_order.BaseOrderResponse
import com.jachai.jachaimart.model.order.details.OrderDetailsResponse
import com.jachai.jachaimart.model.order.history.OrderHistoryResponse
import com.jachai.jachaimart.model.order.multi_order.BaseOrderDetailsResponse
import com.jachai.jachaimart.model.order.multi_order.MultiOrderResponse
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.product.FavouriteProductResponse
import com.jachai.jachaimart.model.response.promo.PromoResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.*

interface OrderService {

    @POST(ApiConstants.ORDER_REQUEST_BASE)
    fun orderRequest(@Body orderRequest: OrderRequest): Call<OrderResponse>

    @POST(ApiConstants.ORDER_REQUEST_HUB_BASE)
    fun orderRequestForHub(@Body orderRequest: OrderRequest): Call<MultiOrderResponse>

    @GET(ApiConstants.ORDER_DETAILS_BASE)
    fun orderDetailsRequest(@Query("orderId") orderID: String): Call<OrderDetailsResponse>

    @GET(ApiConstants.ORDER_DETAILS_V2_BASE)
    fun orderDetailsRequestV2(@Query("BaseOrderId") baseOrderID: String): Call<BaseOrderDetailsResponse>

    //favourite product
    @GET(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun getMyFavoriteProducts(): Call<FavouriteProductResponse>

    @POST(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun setAsMyFavouriteProduct(@Body fProductsItem: FProductsItem): Call<GenericResponse>


    @PUT(ApiConstants.FAVOURITE_PRODUCT_BASE)
    fun removeMyFavouriteProduct(@Body fProductsItem: FProductsItem): Call<GenericResponse>

    @GET(ApiConstants.ORDER_LIST_BASE)
    fun getMyAllOrder(
        @Query("startFrom") startFrom: String,
        @Query("endAt") endAt: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<OrderHistoryResponse>

    //orders v2
    @GET(ApiConstants.ORDER_LIST_V2_BASE)
    fun getMyAllOrderV2(
        @Query("startFrom") startFrom: String,
        @Query("endAt") endAt: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<BaseOrderResponse>

    //promo
    @GET(ApiConstants.REQUEST_ALL_PROMO_BASE)
    fun getMyAllPromo(
    ): Call<PromoResponse>


}