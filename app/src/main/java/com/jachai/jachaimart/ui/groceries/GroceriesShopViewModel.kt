package com.jachai.jachaimart.ui.groceries

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.request.CategoryWithProductRequest
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.response.category.CatWithRelatedProductsResponse
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.CategoryResponse
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

    private var categoryCall: Call<CategoryResponse>? = null
    private var categoryWithProductCall: Call<CatWithRelatedProductsResponse>? = null
    private val orderService = RetrofitConfig.orderService
    private val groceryService = RetrofitConfig.groceryService
    private var favouriteProductCall: Call<FavouriteProductResponse>? = null
    var successCategoryResponseLiveData = MutableLiveData<CategoryResponse?>()
    var successCategoryWithProductResponseLiveData =
        MutableLiveData<CatWithRelatedProductsResponse?>()
    private var errorResponseLiveData = MutableLiveData<String?>()


    fun requestForBanners() {

    }

    fun requestForShopCategories(shopId: String) {
        try {
            if (categoryCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }

            categoryCall = groceryService.getShopCategories(shopId)

            categoryCall?.enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    categoryCall = null
                    successCategoryResponseLiveData.postValue(response.body())
                    JachaiLog.d(TAG, response.body().toString())

                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    categoryCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }

    }

    fun requestForShopCategoryWithRelatedProduct(
        categories: List<CategoriesItem?>?,
        shopID: String
    ) {
        try {

            if (categoryWithProductCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
                return
            }
            val categoryWithProductRequest = CategoryWithProductRequest()

            var mCategoriesIds = mutableListOf<String>()
            if (categories != null) {
                for (item in categories) {
                    item?.id?.let {
                        mCategoriesIds.add(it)
                    }
                }
                categoryWithProductRequest.categories = mCategoriesIds
                categoryWithProductRequest.shopId = shopID

            }

            categoryWithProductCall =
                groceryService.getProductsWithCategory(categoryWithProductRequest)

            categoryWithProductCall?.enqueue(object :
                Callback<CatWithRelatedProductsResponse> {
                override fun onResponse(
                    call: Call<CatWithRelatedProductsResponse>,
                    response: Response<CatWithRelatedProductsResponse>
                ) {
                    categoryWithProductCall = null
                    successCategoryWithProductResponseLiveData.postValue(response.body())
                    JachaiLog.d(TAG, response.body().toString())

                }

                override fun onFailure(
                    call: Call<CatWithRelatedProductsResponse>,
                    t: Throwable
                ) {
                    categoryWithProductCall = null
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }

            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }

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