package com.jachai.jachaimart.ui.initial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.user.model.response.auth.signup.AuthResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel (application: Application) : AndroidViewModel(application) {

    var liveData: MutableLiveData<String> = MutableLiveData()
    private var userInfoCall: Call<AuthResponse>? = null
    private val authService = RetrofitConfig.authService
    

    fun initSplashScreen() {
        viewModelScope.launch {
            delay(2000)
            updateLiveData("login")
        }
    }

    private fun updateLiveData(state: String) {
        liveData.value = state
    }

    fun getUserInfo() {
        if (userInfoCall != null) {
            return
        } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
            // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

            return
        }

        userInfoCall = authService.getUserInfo()

        userInfoCall?.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                userInfoCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> {
                        SharedPreferenceUtil.setName(response.body()?.name)
                        SharedPreferenceUtil.setMobileNo(response.body()?.mobileNumber)
                        liveData.value = "seccess"
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
            }
        })
    }
}