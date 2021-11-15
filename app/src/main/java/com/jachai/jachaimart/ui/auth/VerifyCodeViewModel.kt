package com.jachai.jachaimart.ui.auth

import android.app.Application
import android.os.CountDownTimer
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants
import com.jachai.user.model.response.auth.otp.OtpRequest
import com.jachai.user.model.response.auth.signup.AuthRequest
import com.jachai.user.model.response.auth.signup.AuthResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class VerifyCodeViewModel (application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = VerifyCodeViewModel::class.java
        const val OTP_EXPIRED_MESSAGE = "OTP_EXPIRED"
        private const val DEFAULT_COUNT_DOWN_TIME = 1000 * 60 * 5L
    }

    private val authService = RetrofitConfig.authService
    private var signupCall: Call<AuthResponse>? = null


    private val preferences = JachaiApplication.preferences

    var successResponseLiveData = MutableLiveData<AuthResponse?>()
    var createdResponseLiveData = MutableLiveData<AuthResponse?>()
    var failedResponseLiveData = MutableLiveData<GenericResponse?>()
    val timerLiveData = MutableLiveData<String>()
    private var otpRequestCall: Call<GenericResponse>? = null

    var otpSuccessResponseLiveData = MutableLiveData<String?>()
    var otpErrorResponseLiveData = MutableLiveData<String?>()

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
                    otpErrorResponseLiveData.value = "failed"
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
                            otpSuccessResponseLiveData.value = response.message()
                        }

                        HttpStatusCode.HTTP_OK -> {
                            preferences.edit {
                                putLong(
                                    SharedPreferenceConstants.OTP_REQUEST_TIME_KEY,
                                    System.currentTimeMillis() + DEFAULT_COUNT_DOWN_TIME
                                )
                            }
                            otpSuccessResponseLiveData.value = response.message()
                        }
                        else -> {
                            otpErrorResponseLiveData.value = "failed"
                        }
                    }

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }

    fun performSignupRequest(signupRequest: AuthRequest) {
        if (signupCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

            return
        }

        signupCall = authService.signupRequest(signupRequest)

        signupCall?.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                signupCall = null

                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> {
                        if(response.body()?.statusCode == 200)
                             successResponseLiveData.value = response.body()
                        else
                            createdResponseLiveData.value = response.body()
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

    private val countDownTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        val lastOTPRequestTimer = preferences.getLong(SharedPreferenceConstants.OTP_REQUEST_TIME_KEY, 0)
        val millisInFuture = if ((lastOTPRequestTimer - System.currentTimeMillis()) < 0) {
            0
        } else {
            (lastOTPRequestTimer - System.currentTimeMillis())
        }
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(millisInFuture, 500) {
            override fun onFinish() {
                timerLiveData.value = OTP_EXPIRED_MESSAGE
            }

            override fun onTick(millisUntilFinished: Long) {
                timerLiveData.value = countDownTimeFormat.format(Date(millisUntilFinished))
            }
        }
        countDownTimer?.start()
    }

}