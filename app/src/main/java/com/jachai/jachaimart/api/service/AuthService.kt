package com.jachai.jachaimart.api.service

import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.auth.BiometricLoginRequest
import com.jachai.jachaimart.model.response.auth.BiometricRegisterRequest
import com.jachai.jachaimart.model.response.auth.BiometricRegistrationResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import com.jachai.user.model.response.auth.otp.OtpRequest
import com.jachai.user.model.response.auth.otp.UpdateNameRequest
import com.jachai.jachaimart.model.response.auth.signup.AuthRequest
import com.jachai.user.model.response.auth.signup.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST(ApiConstants.LOGIN_BASE)
    fun signupRequest(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST(ApiConstants.OTP_REQUEST_BASE)
    fun otpRequest(@Body otpRequest: OtpRequest): Call<GenericResponse>

    @POST(ApiConstants.NAME_UPDATE_REQUEST_BASE)
    fun updateNameRequest(@Body updateNameRequest: UpdateNameRequest): Call<GenericResponse>

    @GET(ApiConstants.USER_INFO_REQUEST_BASE)
    fun getUserInfo(): Call<AuthResponse>

    @POST("biometric/register")
    fun registerBiometricRequest(@Body authRequest: BiometricRegisterRequest): Call<BiometricRegistrationResponse>

    @POST("biometric/login")
    fun biometricLoginRequest(@Body authRequest: BiometricLoginRequest): Call<AuthResponse>

}