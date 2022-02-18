package com.jachai.jachaimart.ui.promos

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.product.ProductBySlugResponse
import com.jachai.jachaimart.model.response.promo.PromoResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.ui.product.ProductDetailsViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromosViewModel (application: Application) : BaseViewModel(application) {
    private val orderService = RetrofitConfig.orderService
    private var promoCall: Call<PromoResponse>? = null

    var successPromoCodesResponseLiveData = MutableLiveData<PromoResponse?>()
    var errorPromoCodesResponseLiveData = MutableLiveData<String?>()



    fun getAllPromos() {
        try {
            if (promoCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            promoCall = orderService.getMyAllPromo()

            promoCall?.enqueue(object : Callback<PromoResponse> {
                override fun onResponse(
                    call: Call<PromoResponse>,
                    response: Response<PromoResponse>
                ) {
                    promoCall = null
                    if (response.isSuccessful) {
                        successPromoCodesResponseLiveData.postValue(response.body())
                        JachaiLog.d(ProductDetailsViewModel.TAG, response.body().toString())
                    } else {
                        errorPromoCodesResponseLiveData.value = "failed"
                    }

                }

                override fun onFailure(call: Call<PromoResponse>, t: Throwable) {
                    promoCall = null
                    errorPromoCodesResponseLiveData.value = "failed"
                    JachaiLog.d(ProductDetailsViewModel.TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }

}