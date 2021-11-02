package com.jachai.jachaimart.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call

class RestaurantSearchViewModel  (application: Application) : BaseViewModel(application) {

    private var pageCount = 1
    private var _totalDataCount = 0
    var searchText = ""
    var isRestaurantListShowing = true

    var totalDataCount = 0
        get() = _totalDataCount

    val shopList: MutableList<ShopsItem> by lazy { mutableListOf() }
    val shopResponse: MutableLiveData<List<ShopsItem>> by lazy { MutableLiveData<List<ShopsItem>>() }
    val shopErr: MutableLiveData<String> by lazy { MutableLiveData<String>() }


    private val foodService = RetrofitConfig.foodService
    private var foodSearchRequestCall: Call<List<ShopsItem>>? = null

    fun clear() {
        pageCount = 1
        _totalDataCount = 0
        shopList.clear()
    }

//    fun searchRestaurant() {
//        if (isAllDataFetched()) return
//
//        appRepository.searchDoctors(
//            pageCount,
//            searchText,
//            object : ResponseListener<CommonSuccessResponse<List<DoctorDetailsDto>>, String> {
//                override fun success(response: CommonSuccessResponse<List<DoctorDetailsDto>>) {
//                    response.data?.let {
//                        doctorList.addAll(doctorDetailsMapper.mapToEntityList(it))
//                        _totalDataCount = response.meta?.count ?: 0
//                        doctorResponse.value = doctorList
//                        updatePageCount()
//                        return
//                    }
//                    doctorResponse.value = doctorList
//                }
//
//                override fun error(error: String) {
//                    doctorErr.value = error
//                }
//            })
//    }

    private fun isAllDataFetched(): Boolean {
        return if (isRestaurantListShowing) pageCount > 1 && shopList.size == _totalDataCount
        else false
    }



}