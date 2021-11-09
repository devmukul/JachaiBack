package com.jachai.jachaimart.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.response.category.Product
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.response.search.PopularKeywordResponse
import com.jachai.jachaimart.model.response.search.SearchKeywordResponse
import com.jachai.jachaimart.model.response.search.SearchProductResponse
import com.jachai.jachaimart.model.shop.SearchHistoryItem
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import com.vikas.paging3.data.DoggoImagesRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalPagingApi
class GroceriesSearchViewModel  constructor(val repository: DoggoImagesRepository = DoggoImagesRepository.getInstance()) :
    ViewModel() {

    /**
     * returning non modified PagingData<DoggoImageModel> value as opposite to remote view model
     * where we have mapped the coming values into different object
     */
    fun fetchDoggoImages(key: String): Flow<PagingData<Product>> {
        return repository.letDoggoImagesFlow(key = key).cachedIn(viewModelScope)
    }

    private var pageCount = 1
    private var _totalDataCount = 0
    var searchText = ""
    var isRestaurantListShowing = true
//
//    var totalDataCount = 0
//        get() = _totalDataCount

    val shopList: MutableList<ShopsItem> by lazy { mutableListOf() }
    val shopResponse: MutableLiveData<SearchProductResponse> by lazy { MutableLiveData<SearchProductResponse>() }
    val shopErr: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val groceryService = RetrofitConfig.groceryService
    private var foodSearchRequestCall: Call<SearchProductResponse>? = null

    private var foodSearchKeywordCall: Call<SearchKeywordResponse>? = null
    private var foodPopularSearchCall: Call<PopularKeywordResponse>? = null
    val searhKeywordLiveData: MutableLiveData<SearchKeywordResponse> by lazy { MutableLiveData<SearchKeywordResponse>() }
    val popularKeywordLiveData: MutableLiveData<PopularKeywordResponse> by lazy { MutableLiveData<PopularKeywordResponse>() }

    fun clear() {
        pageCount = 1
        _totalDataCount = 0
        shopList.clear()
    }

    fun searchSuggetion(key: String) {
        foodSearchKeywordCall = groceryService.searchKeyword(key, "JC_MART")

        foodSearchKeywordCall?.enqueue(object : Callback<SearchKeywordResponse> {
            override fun onResponse(
                call: Call<SearchKeywordResponse>?,
                response: Response<SearchKeywordResponse>?
            ) {
                foodSearchRequestCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> searhKeywordLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<SearchKeywordResponse>?, t: Throwable?) {
            }
        })
    }

    fun searchPopularSearch() {
        foodPopularSearchCall = groceryService.searchPopularSearch("JC_MART")

        foodPopularSearchCall?.enqueue(object : Callback<PopularKeywordResponse> {
            override fun onResponse(
                call: Call<PopularKeywordResponse>?,
                response: Response<PopularKeywordResponse>?
            ) {
                foodPopularSearchCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> popularKeywordLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<PopularKeywordResponse>?, t: Throwable?) {
            }
        })
    }



//    private fun isAllDataFetched(): Boolean {
//        return if (isRestaurantListShowing) pageCount > 1 && shopList.size == _totalDataCount
//        else false
//    }

//    private var key: String = ""
//
//    override fun fetchData(vararg arguments: Any) {
//        if (arguments.count() >= 1 && arguments[0] is String) {
//            key = arguments[0] as String
//        }
//        super.fetchData(*arguments)
//    }
//
//    override fun refreshData() {
//        super.refreshData()
//    }


    val serachSuccessList: MutableLiveData<List<SearchHistoryItem>> by lazy { MutableLiveData<List<SearchHistoryItem>>() }


    fun geSearchHistoryList() {
        serachSuccessList.postValue(
            JachaiFoodApplication.mDatabase.daoAccess().getSearchHistory()
        )
    }


}