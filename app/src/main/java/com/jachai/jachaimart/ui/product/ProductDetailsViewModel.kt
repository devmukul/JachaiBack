package com.jachai.jachaimart.ui.product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.model.response.product.ProductDetailsResponse
import com.jachai.jachaimart.model.response.product.Shop
import com.jachai.jachaimart.model.response.product.VariationsItem
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        val TAG = ProductDetailsViewModel::class.java
    }

    private val groceryService = RetrofitConfig.groceryService
    private val orderService = RetrofitConfig.orderService

    private var productDetailsCall: Call<ProductDetailsResponse>? = null
    private var favouriteProductCall: Call<GenericResponse>? = null


//    var successAddToCartData = MutableLiveData<Boolean?>()
    var successProductDetailsResponseLiveData = MutableLiveData<ProductDetailsResponse?>()
    var successFavouriteProductResponseLiveData = MutableLiveData<GenericResponse?>()
    var errorFavouriteProductResponseLiveData = MutableLiveData<String?>()




    fun requestForProductDetails(slug: String) {
        try {
            if (productDetailsCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            productDetailsCall = groceryService.getProductDetails(slug)

            productDetailsCall?.enqueue(object : Callback<ProductDetailsResponse> {
                override fun onResponse(
                    call: Call<ProductDetailsResponse>,
                    response: Response<ProductDetailsResponse>
                ) {
                    productDetailsCall = null
                    successProductDetailsResponseLiveData.postValue(response.body())
                    JachaiLog.d(TAG, response.body().toString())
                }

                override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                    productDetailsCall = null
                    errorResponseLiveData.value = "failed"
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }


    }
/*

    fun saveProduct(item: Product, quantity: Int, shopItem: Shop?, isFromSameShop: Boolean) {
        val productOrder = ProductOrder()
        productOrder.product = item.id.toString()
        productOrder.productName = item.name
//        productOrder.quantity = quantity.toInt()
        productOrder.shopId = shopItem?.id!!
        productOrder.shopName = shopItem.name
        productOrder.shopSubtitle = "na"
        productOrder.shopImage = shopItem.logo
        productOrder.image = item.productImage
        productOrder.isChecked = false
        productOrder.isPopular = false


//        var finalProductOrder =
//            item.variations?.get(0)?.let {
//                updatedProductOrder(productOrder, quantity, it)
//            }
        val finalProductOrder =
            item.variations?.get(0)?.let {
                when {
                    item.variations[0]?.maximumOrderLimit == 0 -> {
                        updatedProductOrder(productOrder, quantity, it, false)
                    }
                    quantity <= item.variations[0]?.maximumOrderLimit!! -> {
                        updatedProductOrder(productOrder, quantity, it, false)

                    }
                    else -> {

                        val tempProductOrder = updatedProductOrder(
                            productOrder,
                            item.variations[0]?.maximumOrderLimit!!,
                            it,
                            true
                        )
                        JachaiApplication.mDatabase.daoAccess()
                            .insertOrder(tempProductOrder, isFromSameShop)
                        val mQuantity = quantity - item.variations[0]?.maximumOrderLimit!!
                        val nextVariationsId = item.variations[0]?.regularVariationId

                        val nextVariationsItem: VariationsItem? =
                            getVariationItemById(item.variations, nextVariationsId)

                        if (nextVariationsItem != null) {
                            updatedProductOrder(productOrder, mQuantity, nextVariationsItem, true)
                        } else {
                            null
                        }

                    }
                }
            }



        successAddToCartData.postValue(
            JachaiApplication.mDatabase.daoAccess().insertOrder(finalProductOrder!!, isFromSameShop)
        )


    }

    private fun getVariationItemById(
        variations: List<VariationsItem?>,
        nextVariationsId: String?
    ): VariationsItem? {
        for (item in variations) {
            if (item != null) {
                if (item.variationId.equals(nextVariationsId)) {
                    return item
                }

            }
        }
        return null
    }

    private fun updatedProductOrder(
        tProductOrder: ProductOrder,
        mQuantity: Int,
        variationsItem: VariationsItem,
        isRegularPrice: Boolean
    ): ProductOrder {

        val tempPrice = variationsItem.price?.mrp ?: 0.0
        val tempDiscountedPrice = variationsItem.price?.discountedPrice ?: 0.0

        val mPrice: Double
        val mDiscountPrice: Double


        when {
            tempDiscountedPrice <= 0.0 -> {
                mPrice = tempPrice
                mDiscountPrice = tempPrice
            }
            tempDiscountedPrice > 0 && tempDiscountedPrice < tempPrice -> {
                mPrice = tempPrice
                mDiscountPrice = tempDiscountedPrice
            }
            else -> {
                mPrice = tempPrice
                mDiscountPrice = tempPrice

            }
        }

        tProductOrder.variant = variationsItem.variationName
        tProductOrder.quantity = mQuantity
        tProductOrder.price = mPrice
        tProductOrder.discountedPrice = mDiscountPrice
        tProductOrder.variationId = variationsItem.variationId ?: ""
        tProductOrder.maximumOrderLimit = variationsItem.maximumOrderLimit
        tProductOrder.stock = variationsItem.stock
        return tProductOrder
    }

*/

    fun requestForSetFavouriteProduct(slug: String) {
        try {
            if (favouriteProductCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            val fProductsItem = FProductsItem(slug)

            favouriteProductCall = orderService.setAsMyFavouriteProduct(fProductsItem)

            favouriteProductCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    favouriteProductCall = null
                    if (response.isSuccessful) {
                        successFavouriteProductResponseLiveData.postValue(response.body())
                        updateFavouriteProductTable(slug, false)
                        JachaiLog.d(TAG, response.body().toString())
                    } else {
                        errorFavouriteProductResponseLiveData.value = "failed"
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    favouriteProductCall = null
                    errorFavouriteProductResponseLiveData.value = "failed"
                    JachaiLog.d(TAG, errorResponseLiveData.value.toString())

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }


    }


    fun requestForRemoveFavouriteProduct(slug: String) {
        try {
            if (favouriteProductCall != null) {
                return
            } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
                getApplication<JachaiApplication>().showShortToast(R.string.networkError)
                return
            }
            val fProductsItem = FProductsItem(slug)

            favouriteProductCall = orderService.removeMyFavouriteProduct(fProductsItem)

            favouriteProductCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    favouriteProductCall = null
                    if (response.isSuccessful) {
                        successFavouriteProductResponseLiveData.postValue(response.body())
                        updateFavouriteProductTable(slug, true)
                        JachaiLog.d(TAG, response.body().toString())
                    } else {
                        errorFavouriteProductResponseLiveData.value = "failed"

                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    favouriteProductCall = null
                    errorFavouriteProductResponseLiveData.value = "failed"
                    JachaiLog.d(TAG, t.localizedMessage)

                }
            })


        } catch (ex: Exception) {
            JachaiLog.d(TAG, ex.message!!)
        }


    }

    private fun updateFavouriteProductTable(slug: String, isDelete: Boolean?) {
        val fProductsItem = FProductsItem(slug)
        if (isDelete == false) {
            JachaiApplication.mDatabase.daoAccess()
                .insertFavouriteProduct(listOf(fProductsItem))
        } else {
            JachaiApplication.mDatabase.daoAccess().deleteFavouriteProduct(slug)
        }
    }

    fun isProductFavourite(slug: String): Boolean {
        return JachaiApplication.mDatabase.daoAccess().getFavouriteProductById(slug).isEmpty()
    }


}