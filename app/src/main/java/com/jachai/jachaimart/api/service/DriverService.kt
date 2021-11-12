package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.address.AddressResponse
import com.jachai.jachaimart.model.response.category.CatWithRelatedProductsResponse
import com.jachai.jachaimart.model.response.location.LocationDetails
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.http.*

interface DriverService {

    @POST(ApiConstants.SAVE_ADDRESS_BASE)
    fun saveUserAddress(@Body locationDetails: LocationDetails): Call<GenericResponse>

    @GET(ApiConstants.GET_ADDRESSES_BASE)
    fun getAllUserAddress(): Call<AddressResponse>

    @PUT(ApiConstants.SAVE_ADDRESS_BASE)
    fun deleteUserAddress(@Query("addressId") addressId: String): Call<AddressResponse>



}