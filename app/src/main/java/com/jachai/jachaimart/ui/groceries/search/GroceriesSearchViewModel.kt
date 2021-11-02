package com.jachai.jachaimart.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.response.product.CategoryDetailsResponse
import com.jachai.jachaimart.model.response.search.SearchProductResponse
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroceriesSearchViewModel  (application: Application) : BaseViewModel(application) {

    private var pageCount = 1
    private var _totalDataCount = 0
    var searchText = ""
    var isRestaurantListShowing = true

    var totalDataCount = 0
        get() = _totalDataCount

    val shopList: MutableList<ShopsItem> by lazy { mutableListOf() }
    val shopResponse: MutableLiveData<SearchProductResponse> by lazy { MutableLiveData<SearchProductResponse>() }
    val shopErr: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val groceryService = RetrofitConfig.groceryService
    private var foodSearchRequestCall: Call<SearchProductResponse>? = null

    fun clear() {
        pageCount = 1
        _totalDataCount = 0
        shopList.clear()
    }

    fun searchGrocery(key: String) {
        if (isAllDataFetched()) return

        if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
            return
        }

        foodSearchRequestCall = groceryService.searchProducts("JC_MART", key, 1, 20)

        foodSearchRequestCall?.enqueue(object : Callback<SearchProductResponse> {
            override fun onResponse(
                call: Call<SearchProductResponse>?,
                response: Response<SearchProductResponse>?
            ) {
                foodSearchRequestCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> shopResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<SearchProductResponse>?, t: Throwable?) {
            }
        })
    }

    private fun isAllDataFetched(): Boolean {
        return if (isRestaurantListShowing) pageCount > 1 && shopList.size == _totalDataCount
        else false
    }



}