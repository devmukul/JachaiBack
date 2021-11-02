package com.jachai.jachaimart.api.service


import com.jachai.jachaimart.model.request.CategoryWithProductRequest
import com.jachai.jachaimart.model.response.category.CatWithRelatedProductsResponse
import com.jachai.jachaimart.model.response.home.BannerResponse
import com.jachai.jachaimart.model.response.home.CategoryResponse
import com.jachai.jachaimart.model.response.home.RestaurantNearMeResponse
import com.jachai.jachaimart.model.response.product.CategoryDetailsResponse
import com.jachai.jachaimart.model.response.product.ProductDetailsResponse
import com.jachai.jachaimart.model.response.search.SearchProductResponse
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.constant.ApiConstants
import retrofit2.Call
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
    fun getRestaurantAroundMe(
        @Query("type") type: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<RestaurantNearMeResponse>

    @GET(ApiConstants.PRODUCT_BY_CAT_BASE)
    fun getShopDetails(@Query("shopId") shopId: String): Call<ShopDetailsResponse>

    @GET(ApiConstants.SHOP_CATEGORIES_BASE)
    fun getShopCategories(@Query("shopId") shopId: String): Call<CategoryResponse>

    @GET(ApiConstants.PRODUCT_DETAILS_BASE)
    fun getProductDetails(@Query("slug") slug: String): Call<ProductDetailsResponse>

    @POST(ApiConstants.CATEGORY_WITH_PRODUCTS_BASE)
    fun getProductsWithCategory(@Body categoryWithProductRequest: CategoryWithProductRequest): Call<CatWithRelatedProductsResponse>

    @GET(ApiConstants.SHOP_CATEGORIES_DETAILS_BASE)
    fun getShopCategoriesDetails(@Query("shopId") shopId: String, @Query("categoryId") categoryId: String): Call<CategoryDetailsResponse>

    @GET(ApiConstants.SEARCH_PRODUCT_BASE)
    fun searchProducts(
        @Query("type") type: String,
        @Query("key") latitude: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<SearchProductResponse>


}