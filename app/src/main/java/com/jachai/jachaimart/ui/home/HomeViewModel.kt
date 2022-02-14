package com.jachai.jachaimart.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.home.RestaurantNearMeResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        val TAG = HomeViewModel::class.java
    }

    private val foodService = RetrofitConfig.foodService

//    private var bannerCall: Call<BannerResponse>? = null
    private var categoryCall: Call<CategoryResponse>? = null
    private var restaurantAroundYouCall: Call<RestaurantNearMeResponse>? = null

//    var successBannerResponseLiveData = MutableLiveData<BannerResponse?>()
    var successCategoryResponseLiveData = MutableLiveData<CategoryResponse?>()
    var successRestaurantAroundYouResponseLiveData = MutableLiveData<RestaurantNearMeResponse?>()






    fun requestForCategories() {
        try {
            if (categoryCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            categoryCall = foodService.getAllCategories(CommonConstants.JC_FOOD_TYPE)

            categoryCall?.enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    categoryCall = null
                    successCategoryResponseLiveData.postValue(response.body())
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    categoryCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(HomeViewModel.TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }


    fun requestForRestaurantAroundYou(latitude: Double?, longitude: Double?) {
        try {
            if (restaurantAroundYouCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }


            restaurantAroundYouCall = foodService.getRestaurantAroundMe(
                CommonConstants.JC_FOOD_TYPE,
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                0,
                20

            )

            restaurantAroundYouCall?.enqueue(object : Callback<RestaurantNearMeResponse> {
                override fun onResponse(
                    call: Call<RestaurantNearMeResponse>,
                    response: Response<RestaurantNearMeResponse>
                ) {
                    restaurantAroundYouCall = null
                    successRestaurantAroundYouResponseLiveData.postValue(response.body())
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())

                }

                override fun onFailure(call: Call<RestaurantNearMeResponse>, t: Throwable) {
                    restaurantAroundYouCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(HomeViewModel.TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }


}