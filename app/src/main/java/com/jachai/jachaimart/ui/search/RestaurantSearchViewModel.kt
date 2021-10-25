package com.jachai.jachaimart.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.ui.base.BaseViewModel
import kotlinx.coroutines.Job

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

    fun clear() {
        pageCount = 1
        _totalDataCount = 0
        shopList.clear()
    }


}