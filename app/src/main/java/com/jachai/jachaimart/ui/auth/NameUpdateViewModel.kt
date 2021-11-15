package com.jachai.jachaimart.ui.auth

import android.app.Application
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
import com.jachai.jachaimart.utils.SharedPreferenceUtil
import com.jachai.user.model.response.auth.otp.UpdateNameRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameUpdateViewModel (application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = NameUpdateViewModel::class.java
    }

    private val preferences = JachaiApplication.preferences
    private val authService = RetrofitConfig.authService
    private var nameRequestCall: Call<GenericResponse>? = null

    var successResponseLiveData = MutableLiveData<String?>()
    var errorResponseLiveData = MutableLiveData<String?>()

    fun registerName(name: String) {
        try {
            if (nameRequestCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            nameRequestCall = authService.updateNameRequest(UpdateNameRequest(fullName = name))

            nameRequestCall?.enqueue(object : Callback<GenericResponse> {
                override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                    nameRequestCall = null
                    errorResponseLiveData.value = "failed"
                }

                override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                    nameRequestCall = null
                    when (response?.code()) {

                        HttpStatusCode.HTTP_CREATED -> {

                            SharedPreferenceUtil.setName(name)
                            successResponseLiveData.value = response!!.message()
                        }

                        HttpStatusCode.HTTP_OK -> {

                            SharedPreferenceUtil.setName(name)
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
}