package com.jachai.jachaimart.ui.product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachai_driver.utils.showShortToast
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.R
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.response.product.Product
import com.jachai.jachaimart.model.response.product.ProductDetailsResponse
import com.jachai.jachaimart.model.response.product.Shop
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

    var successAddToCartData = MutableLiveData<Boolean?>()
    private var productDetailsCall: Call<ProductDetailsResponse>? = null
    var successProductDetailsResponseLiveData = MutableLiveData<ProductDetailsResponse?>()
    var errorResponseLiveData = MutableLiveData<String?>()


    fun requestForProductDetails(slug: String) {
        try {
            if (productDetailsCall != null) {
                return
            } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
                getApplication<JachaiFoodApplication>().showShortToast(R.string.networkError)
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

    fun saveProduct(item: Product, quantity: Int, shopItem: Shop?, isFromSameShop: Boolean) {
        val productOrder = ProductOrder()
        productOrder.product = item.id.toString()
        productOrder.productName = item.name
        productOrder.quantity = quantity.toInt()
        productOrder.shopId = shopItem?.id!!
        productOrder.shopName = shopItem.name
        productOrder.shopSubtitle = "na"
        productOrder.shopImage = shopItem.logo
        productOrder.image = item.productImage
        productOrder.isChecked = false
        productOrder.isPopular = false
        productOrder.variationId = item.variations?.get(0)?.variationId


        val tempPrice = item.variations?.get(0)?.price?.mrp ?: 0.0
        val tempDiscountedPrice = item.variations?.get(0)?.price?.discountedPrice ?: 0.0

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

        productOrder.price = mPrice
        productOrder.discountedPrice = mDiscountPrice

        successAddToCartData.postValue(
            JachaiFoodApplication.mDatabase.daoAccess().insertOrder(productOrder, isFromSameShop)
        )


    }


}