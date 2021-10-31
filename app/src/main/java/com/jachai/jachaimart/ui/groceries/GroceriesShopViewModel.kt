package com.jachai.jachaimart.ui.groceries

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.response.product.FavouriteProductResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.product.ProductDetailsViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroceriesShopViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        val TAG = GroceriesShopViewModel::class.java
    }

    private val orderService = RetrofitConfig.orderService
    private var favouriteProductCall: Call<FavouriteProductResponse>? = null


    fun requestForBanners() {

    }

    fun requestForShopCategories() {

    }

    fun requestForShopCategoryWithRelatedProduct() {

    }

    fun requestAllFavouriteProduct() {
        try {
            if (favouriteProductCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            favouriteProductCall = orderService.getMyFavoriteProducts()

            favouriteProductCall?.enqueue(object : Callback<FavouriteProductResponse> {
                override fun onResponse(
                    call: Call<FavouriteProductResponse>,
                    response: Response<FavouriteProductResponse>
                ) {
                    favouriteProductCall = null
                    JachaiFoodApplication.mDatabase.daoAccess()
                        .insertFavouriteProduct(response.body()?.products as List<FProductsItem>)

                    JachaiLog.d(ProductDetailsViewModel.TAG, response.body().toString())
                }

                override fun onFailure(call: Call<FavouriteProductResponse>, t: Throwable) {
                    favouriteProductCall = null
                    JachaiLog.d(ProductDetailsViewModel.TAG, t.localizedMessage)
                }

            })


        } catch (e: Exception) {
            JachaiLog.d(TAG, e.message!!)
        }

    }


}