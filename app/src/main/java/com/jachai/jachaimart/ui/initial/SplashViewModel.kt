package com.jachai.jachaimart.ui.initial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.response.grocery.NearestJCShopResponse
import com.jachai.jachaimart.ui.base.BaseFragment.Companion.TAG
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.user.model.response.auth.signup.AuthResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    var liveData: MutableLiveData<String> = MutableLiveData()
    private var userInfoCall: Call<AuthResponse>? = null
    private var nearestJCShopCall: Call<NearestJCShopResponse>? = null
    private val authService = RetrofitConfig.authService
    private val groceryService = RetrofitConfig.groceryService


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

    fun getNearestJCShop() {
        try {

            if (nearestJCShopCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                // alertMessageListener?.showAlertMessage(R.string.no_internet_connection_message, AlertType.Snackbar)

                return
            }

            nearestJCShopCall = groceryService.getNearestJCShopAroundMe(
                CommonConstants.JC_MART_TYPE,
                23.76477215668654,
                90.43168098056175,
                0,
                1

            )

            nearestJCShopCall?.enqueue(object : Callback<NearestJCShopResponse> {
                override fun onResponse(
                    call: Call<NearestJCShopResponse>?,
                    response: Response<NearestJCShopResponse>?
                ) {
                    nearestJCShopCall = null
                    when (response?.code()) {
                        HttpStatusCode.HTTP_OK -> {
                            val mResponse = response.body()
                            if (mResponse != null) {
                                if (mResponse.shops.isNotEmpty())
                                    SharedPreferenceUtil.setJCShopId(mResponse.shops[0].id)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<NearestJCShopResponse>?, t: Throwable?) {
                    nearestJCShopCall = null
                    if (t != null) {
                        t.message?.let { JachaiLog.e(TAG, it) }
                    }
                }
            })


        } catch (ex: Exception) {
            JachaiLog.e(TAG, ex.localizedMessage)
        }


    }
}