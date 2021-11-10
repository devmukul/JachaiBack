package com.jachai.jachaimart.ui.favourite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.request.FProductRequest
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.response.product.ProductBySlugResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.ui.product.ProductDetailsViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteViewModel(application: Application) : BaseViewModel(application) {

    private val groceryService = RetrofitConfig.groceryService
    private var slugProductCall: Call<ProductBySlugResponse>? = null

    var successSlugProductResponseLiveData = MutableLiveData<ProductBySlugResponse?>()
    var errorSlugProductResponseLiveData = MutableLiveData<String?>()


    fun getAllFavouriteProducts() {

        val slugList: List<FProductsItem> =
            JachaiFoodApplication.mDatabase.daoAccess().getAllFavouriteProducts()

        val mSlugs = mutableListOf<String>()

        if (!slugList.isNullOrEmpty()) {
            for (item in slugList) {
                mSlugs.add(item.productId)
            }
            val fProductRequest = FProductRequest()
            fProductRequest.slugs = mSlugs
            getAllFavouriteProductsBySlugs(fProductRequest)
        } else {
            errorSlugProductResponseLiveData.postValue("empty")
        }


    }


    fun getAllFavouriteProductsBySlugs(fProductRequest: FProductRequest) {
        try {
            if (slugProductCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }
            slugProductCall = groceryService.getProductBySlugs(fProductRequest)

            slugProductCall?.enqueue(object : Callback<ProductBySlugResponse> {
                override fun onResponse(
                    call: Call<ProductBySlugResponse>,
                    response: Response<ProductBySlugResponse>
                ) {
                    slugProductCall = null
                    if (response.isSuccessful) {
                        successSlugProductResponseLiveData.postValue(response.body())
                        JachaiLog.d(ProductDetailsViewModel.TAG, response.body().toString())
                    } else {
                        errorSlugProductResponseLiveData.value = "failed"
                    }

                }

                override fun onFailure(call: Call<ProductBySlugResponse>, t: Throwable) {
                    slugProductCall = null
                    errorSlugProductResponseLiveData.value = "failed"
                    JachaiLog.d(ProductDetailsViewModel.TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }


    }

}