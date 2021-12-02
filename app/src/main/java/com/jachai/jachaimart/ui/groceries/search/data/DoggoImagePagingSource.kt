package com.vikas.paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jachai.jachaimart.api.service.GroceryService
import com.jachai.jachaimart.model.response.product.Product
import com.vikas.paging3.data.DoggoImagesRepository.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

/**
 * provides the data source for paging lib from api calls
 */
@ExperimentalPagingApi
class DoggoImagePagingSource(val doggoApiService: GroceryService, val key: String, val shopId: String) :
    PagingSource<Int, Product>() {

    /**
     * calls api if there is any error getting results then return the [LoadResult.Error]
     * for successful response return the results using [LoadResult.Page] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [null] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = doggoApiService.searchProducts(key = key, shopId = shopId, page = page, limit= params.loadSize).execute()

            val data = response.body()
            val products = data!!.products
            LoadResult.Page(
                products, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}