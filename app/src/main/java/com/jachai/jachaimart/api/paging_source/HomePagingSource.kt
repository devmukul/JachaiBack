package com.jachai.jachaimart.api.paging_source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct
import com.jachai.jachaimart.ui.base.BaseFragment.Companion.TAG
import com.jachai.jachaimart.utils.RetrofitConfig

class HomePagingSource(val hubId: String?) : PagingSource<Int, CatWithRelatedProduct>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatWithRelatedProduct> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = RetrofitConfig.groceryService.getProductsByCategoryHub(
                hubId,
                nextPageNumber,
                params.loadSize
            )
            return if (response.body()?.currentPageNumber!! < response.body()?.totalPages!!) {
                LoadResult.Page(
                    data = response.body()?.catWithRelatedProducts!!,
                    prevKey = null,
                    nextKey = response.body()?.currentPageNumber!!.plus(1)
                )
            } else {
                LoadResult.Page(
                    data = response.body()?.catWithRelatedProducts!!,
                    prevKey = null,
                    nextKey = null
                )
            }


        } catch (exception: Exception) {
            JachaiLog.e(TAG,exception.toString())
            return LoadResult.Error(exception)
        }


    }


    override fun getRefreshKey(state: PagingState<Int, CatWithRelatedProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}