package com.jachai.jachaimart.api.service


import com.jachai.jachaimart.model.request.CategoryWithProductRequest
import com.jachai.jachaimart.model.request.FProductRequest
import com.jachai.jachaimart.model.response.category.CatWithRelatedProductsResponse
import com.jachai.jachaimart.model.response.grocery.NearestJCShopResponse
import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.product.CategoryDetailsResponse
import com.jachai.jachaimart.model.response.product.ProductBySlugResponse
import com.jachai.jachaimart.model.response.product.ProductDetailsResponse
import com.jachai.jachaimart.model.response.search.PopularKeywordResponse
import com.jachai.jachaimart.model.response.search.SearchKeywordResponse
import com.jachai.jachaimart.model.response.search.SearchProductResponse
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroceryService {

    @GET(ApiConstants.BANNER_BASE)
    fun getAllHomeBanners(@Query("type") type: String): Call<BannerResponse>


    @GET(ApiConstants.CATEGORY_BASE)
    fun getAllCategories(@Query("type") type: String): Call<CategoryResponse>


    @GET(ApiConstants.SHOP_NEAREST_BASE)
    fun getNearestJCShopAroundMe(
        @Query("type") type: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<NearestJCShopResponse>


   @GET(ApiConstants.PRODUCT_BY_CATEGORY_BASE)
    suspend fun getProductsByCategoryShop(
        @Query("shopId") type: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<CatWithRelatedProductsResponse>

    @GET(ApiConstants.PRODUCT_BY_CAT_BASE)
    fun getShopDetails(@Query("shopId") shopId: String): Call<ShopDetailsResponse>

    @GET(ApiConstants.SHOP_CATEGORIES_BASE)
    fun getShopCategories(@Query("shopId") shopId: String): Call<CategoryResponse>

    @GET(ApiConstants.PRODUCT_DETAILS_BASE)
    fun getProductDetails(@Query("slug") slug: String): Call<ProductDetailsResponse>

    @POST(ApiConstants.CATEGORY_WITH_PRODUCTS_BASE)
    fun getProductsWithCategory(@Body categoryWithProductRequest: CategoryWithProductRequest): Call<CatWithRelatedProductsResponse>

    @GET(ApiConstants.SHOP_CATEGORIES_DETAILS_BASE)
    fun getShopCategoriesDetails(
        @Query("shopId") shopId: String,
        @Query("categoryId") categoryId: String
    ): Call<CategoryDetailsResponse>


    @GET(ApiConstants.SEARCH_PRODUCT_BASE)
    fun searchProducts(
        @Query("key") key: String,
        @Query("type") type: String = "JC_MART",
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20
    ): Call<SearchProductResponse>

    @GET("search/suggestion")
    fun searchKeyword(
        @Query("key") key: String,
        @Query("type") type: String = "JC_MART"
    ): Call<SearchKeywordResponse>

    @GET("search/popular")
    fun searchPopularSearch(
        @Query("type") type: String = "JC_MART"
    ): Call<PopularKeywordResponse>


    @POST(ApiConstants.PRODUCT_BY_SLUG_BASE)
    fun getProductBySlugs(@Body fProductRequest: FProductRequest): Call<ProductBySlugResponse>


}