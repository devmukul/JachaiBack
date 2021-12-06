package com.jachai.jachaimart.ui.initial

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.user.model.response.auth.signup.AuthResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel(application: Application) : BaseViewModel(application) {

    var liveData: MutableLiveData<String> = MutableLiveData()
    private var userInfoCall: Call<AuthResponse>? = null
    private val authService = RetrofitConfig.authService


    fun initSplashScreen() {
        viewModelScope.launch {
            delay(1000)
            updateLiveData("login")
        }
    }

    private fun updateLiveData(state: String) {
        liveData.value = state
    }

    fun getUserInfo() {
        if (userInfoCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            getApplication<JachaiApplication>().showShortToast(R.string.networkError)
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
                    else ->{
                        liveData.value = "login"
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                userInfoCall = null
                liveData.value = "login"
            }
        })
    }


}