package com.jachai.jachaimart.api.service


import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.home.RestaurantNearMeResponse
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodService {

    @GET(ApiConstants.BANNER_BASE)
    fun getAllHomeBanners(@Query("type") type: String): Call<BannerResponse>


    @GET(ApiConstants.CATEGORY_BASE)
    fun getAllCategories(@Query("type") type: String): Call<CategoryResponse>


    @GET(ApiConstants.SHOP_NEAREST_BASE)
    fun getRestaurantAroundMe(
        @Query("type") type: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<RestaurantNearMeResponse>

    @GET(ApiConstants.PRODUCT_BY_CAT_BASE)
    fun getShopDetails(@Query("shopId") shopId: String): Call<ShopDetailsResponse>

}