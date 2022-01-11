package com.jachai.jachaimart.ui.groceries

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.getDeviceId
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.api.paging_source.HomePagingSource
import com.jachai.jachaimart.decorator.CryptographyManager
import com.jachai.jachaimart.model.notification.NotificationRegisterRequest
import com.jachai.jachaimart.model.request.CategoryWithProductRequest
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.auth.BiometricRegisterRequest
import com.jachai.jachaimart.model.response.auth.BiometricRegistrationResponse
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.model.response.category.CatWithRelatedProductsResponse
import com.jachai.jachaimart.model.response.home.CategoriesItem
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.product.FavouriteProductResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.ui.product.ProductDetailsViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.ApiConstants.PAGING_PAGE_LIMIT_MIN
import com.jachai.jachaimart.utils.constant.SharedPreferenceConstants
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroceriesShopViewModel(application: Application) : BaseViewModel(application) {

    companion object {
        val TAG = GroceriesShopViewModel::class.java
    }

    var successOnGoingOrderFound = MutableLiveData<Int>()
    val isRefresh = MutableLiveData<Boolean>()

    private var categoryCall: Call<CategoryResponse>? = null
    private var categoryWithProductCall: Call<CatWithRelatedProductsResponse>? = null
    private val orderService = RetrofitConfig.orderService
    private val groceryService = RetrofitConfig.groceryService
    private var favouriteProductCall: Call<FavouriteProductResponse>? = null
    var successCategoryResponseLiveData = MutableLiveData<CategoryResponse?>()
    var successCategoryWithProductResponseLiveData =
        MutableLiveData<CatWithRelatedProductsResponse?>()

    var errorCategoryWithProductResponseLiveData = MutableLiveData<String?>()
    private val notificationsService = RetrofitConfig.notificationsService
    private val authService = RetrofitConfig.authService
    private var registerCall: Call<GenericResponse>? = null

    private var registerBiometricCall: Call<BiometricRegistrationResponse>? = null

    var successBiometricRegLiveData =
        MutableLiveData<BiometricRegistrationResponse?>()





    fun requestForBanners() {

    }

    fun requestForShopCategories(hubId: String) {
        isRefresh.postValue(true)
        try {
            if (categoryCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            categoryCall = groceryService.getHubCategories(hubId)

            categoryCall?.enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    categoryCall = null
                    isRefresh.postValue(false)
                    successCategoryResponseLiveData.postValue(response.body())
                    JachaiLog.d(TAG, response.body().toString())

                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    categoryCall = null
                    isRefresh.postValue(false)
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
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
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
                    errorCategoryWithProductResponseLiveData.postValue("Failed")
                    categoryWithProductCall = null
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }

            })

        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
            errorCategoryWithProductResponseLiveData.postValue("Failed")
        }

    }

    fun requestAllFavouriteProduct() {
        try {
            if (favouriteProductCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }

            favouriteProductCall = orderService.getMyFavoriteProducts()

            favouriteProductCall?.enqueue(object : Callback<FavouriteProductResponse> {
                override fun onResponse(
                    call: Call<FavouriteProductResponse>,
                    response: Response<FavouriteProductResponse>
                ) {
                    favouriteProductCall = null
                    JachaiApplication.mDatabase.daoAccess()
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

    fun getCurrentOrderStatus() {
        val db = JachaiApplication.mDatabase.daoAccess()
        if (db.getOnGoingOrderCount() >0){
            successOnGoingOrderFound.postValue(db.getOnGoingOrderCount())
        }
    }

    fun requestForShopCategoryWithRelatedProduct(hubId: String?): Flow<PagingData<CatWithRelatedProduct>> {
        return Pager(
            PagingConfig(
                pageSize = PAGING_PAGE_LIMIT_MIN,
                initialLoadSize = PAGING_PAGE_LIMIT_MIN
            )
        ) {
//            HomePagingSource(shopId)
            HomePagingSource(hubId = hubId)
        }.flow.cachedIn(viewModelScope)

    }

    fun requestRegister(activity: Activity, fcmToken: String) {
        try {
            if (registerCall != null) {
                return
            }
            val notificationRegisterRequest = NotificationRegisterRequest(activity.getDeviceId(), "Android", "${Build.BRAND}_${Build.MODEL}", fcmToken)

            registerCall = notificationsService.registerDevice(notificationRegisterRequest)

            registerCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    registerCall = null
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    registerCall = null

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }


    fun requestBiometricRegister(activity: Activity) {
        try {
            if (registerBiometricCall != null) {
                return
            }
            val notificationRegisterRequest = BiometricRegisterRequest(activity.getDeviceId())

            registerBiometricCall = authService.registerBiometricRequest(notificationRegisterRequest)

            registerBiometricCall?.enqueue(object : Callback<BiometricRegistrationResponse> {
                override fun onResponse(
                    call: Call<BiometricRegistrationResponse>,
                    response: Response<BiometricRegistrationResponse>
                ) {
                    registerBiometricCall = null
                    successBiometricRegLiveData.postValue(response.body())
                }

                override fun onFailure(call: Call<BiometricRegistrationResponse>, t: Throwable) {
                    registerBiometricCall = null

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }


}