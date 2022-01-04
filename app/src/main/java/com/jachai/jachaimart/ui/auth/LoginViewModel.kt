package com.jachai.jachaimart.ui.auth

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.auth.BiometricLoginRequest
import com.jachai.jachaimart.model.response.auth.signup.AuthRequest
import com.jachai.jachaimart.model.response.biometricLoginRequest
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants
import com.jachai.user.model.response.auth.otp.OtpRequest
import com.jachai.user.model.response.auth.signup.AuthResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = LoginViewModel::class.java

        const val OTP_EXPIRED_MESSAGE = "OTP_EXPIRED"
        const val DEFAULT_COUNT_DOWN_TIME = 1000 * 60 * 5L
    }

    private val preferences = JachaiApplication.preferences

    private val authService = RetrofitConfig.authService
    private var otpRequestCall: Call<GenericResponse>? = null

    var successResponseLiveData = MutableLiveData<String?>()
    var errorResponseLiveData = MutableLiveData<String?>()

    private var signupCall: Call<AuthResponse>? = null
    var biometricSuccessResponseLiveData = MutableLiveData<AuthResponse?>()
    var failedResponseLiveData = MutableLiveData<GenericResponse?>()

    fun registerMobileNumber(mobileNumber: String) {
        try {
            if (otpRequestCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            otpRequestCall = authService.otpRequest(OtpRequest(mobileNumber = mobileNumber))

            otpRequestCall?.enqueue(object : Callback<GenericResponse> {
                override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                    otpRequestCall = null
                    errorResponseLiveData.value = "failed"
                }

                override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                    otpRequestCall = null
                    when (response?.code()) {

                        HttpStatusCode.HTTP_CREATED -> {
                            preferences.edit {
                                putLong(
                                    SharedPreferenceConstants.OTP_REQUEST_TIME_KEY,
                                    System.currentTimeMillis() + DEFAULT_COUNT_DOWN_TIME
                                )
                            }
                            successResponseLiveData.value = response!!.message()
                        }

                        HttpStatusCode.HTTP_OK -> {
                            preferences.edit {
                                putLong(
                                    SharedPreferenceConstants.OTP_REQUEST_TIME_KEY,
                                    System.currentTimeMillis() + DEFAULT_COUNT_DOWN_TIME
                                )
                            }
                            successResponseLiveData.value = response!!.message()
                        }
                        else -> {
                            errorResponseLiveData.value = "failed"
                        }
                    }

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }


    fun performBiometricRequest(signupRequest: BiometricLoginRequest) {
        if (signupCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {

            return
        }

        signupCall = authService.biometricLoginRequest(signupRequest)

        signupCall?.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                signupCall = null

                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> {
                        biometricSuccessResponseLiveData.value = response.body()
                    }
                    else -> {
                        val jObjError = JSONObject(response!!.errorBody()!!.string())
                        failedResponseLiveData.value =
                            CommonConstants.DEFAULT_NON_NULL_GSON.fromJson(
                                jObjError.toString(), GenericResponse::class.java
                            ) ?: GenericResponse()
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                signupCall = null
            }
        })
    }
}