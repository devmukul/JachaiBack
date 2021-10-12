package com.jachai.jachaimart.service

import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.user.model.response.GenericResponse
import com.jachai.user.model.response.auth.login.LoginRequest
import com.jachai.user.model.response.auth.otp.OtpRequest
import com.jachai.user.model.response.auth.otp.UpdateNameRequest
import com.jachai.user.model.response.auth.signup.AuthRequest
import com.jachai.user.model.response.auth.signup.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodService {

    @GET(ApiConstants.PRODUCT_BY_CAT_BASE)
    fun getShopDetails(@Query("shopId") shopId: String): Call<ShopDetailsResponse>

}