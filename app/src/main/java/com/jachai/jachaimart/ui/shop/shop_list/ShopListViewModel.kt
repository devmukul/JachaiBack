package com.jachai.jachaimart.ui.shop.shop_list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.category.ShopByCategoryResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopListViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        val TAG = ShopListViewModel::class.java
    }

    private var shopByCategoryCall: Call<ShopByCategoryResponse>? = null


    private val foodService = RetrofitConfig.foodService


    var successShopsByCategoryResponseLiveData = MutableLiveData<ShopByCategoryResponse?>()
    var errorResponseLiveData = MutableLiveData<String?>()


    fun requestForShopsByCategories(categoryId: String, latitude: Double?, longitude: Double?) {
        try {
            if (shopByCategoryCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            shopByCategoryCall = foodService.getAllShopsByCategory(
                categoryId = categoryId,
                latitude = latitude.toString(),
                longitude = longitude.toString()
            )

            shopByCategoryCall?.enqueue(object : Callback<ShopByCategoryResponse> {
                override fun onResponse(
                    call: Call<ShopByCategoryResponse>,
                    response: Response<ShopByCategoryResponse>
                ) {
                    shopByCategoryCall = null
                    successShopsByCategoryResponseLiveData.postValue(response.body())
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<ShopByCategoryResponse>, t: Throwable) {
                    shopByCategoryCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(HomeViewModel.TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }

}