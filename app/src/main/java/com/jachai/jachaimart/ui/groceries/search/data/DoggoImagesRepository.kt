package com.vikas.paging3.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.jachai.jachaimart.api.service.GroceryService
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.utils.RetrofitConfig
import kotlinx.coroutines.flow.Flow

/**
 * repository class to manage the data flow and map it if needed
 */
@ExperimentalPagingApi
class DoggoImagesRepository(
    val doggoApiService: GroceryService = RetrofitConfig.groceryService
) {

    companion object {
        const val DEFAULT_PAGE_INDEX = 0
        const val DEFAULT_PAGE_SIZE = 20

        //get doggo repository instance
        fun getInstance() = DoggoImagesRepository()
    }

    /**
     * calling the paging source to give results from api calls
     * and returning the results in the form of flow [Flow<PagingData<DoggoImageModel>>]
     * since the [PagingDataAdapter] accepts the [PagingData] as the source in later stage
     */
    fun letDoggoImagesFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        key: String,
        shopId: String
    ): Flow<PagingData<Product>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService, key, shopId) }
        ).flow
    }


    //for live data users
    fun letDoggoImagesLiveData(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        key: String,
        shopId: String
    ): LiveData<PagingData<Product>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService, key, shopId) }
        ).liveData
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }


}