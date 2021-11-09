package com.jachai.jachaimart.ui.shop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.shop.ProductX
import com.jachai.jachaimart.model.shop.ShopDetailsResponse
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private var shopDetailsCall: Call<ShopDetailsResponse>? = null
    private val foodService = RetrofitConfig.foodService
    var successResponseLiveData = MutableLiveData<ShopDetailsResponse?>()
    var successAddToCartData = MutableLiveData<Boolean?>()

    fun getShopDetails(shopId: String) {
        if (shopDetailsCall != null) {
            return
        } else if (!getApplication<JachaiFoodApplication>().isConnectionAvailable()) {
            return
        }

        shopDetailsCall = foodService.getShopDetails(shopId)

        shopDetailsCall?.enqueue(object : Callback<ShopDetailsResponse> {
            override fun onResponse(
                call: Call<ShopDetailsResponse>?,
                response: Response<ShopDetailsResponse>?
            ) {
                shopDetailsCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> successResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<ShopDetailsResponse>?, t: Throwable?) {
            }
        })
    }

    fun saveProduct(item: ProductX, quantity: Int, shopItem: ShopsItem, isFromSameShop: Boolean) {
        val productOrder = ProductOrder()
        productOrder.product = item.id
        productOrder.productName = item.name
        productOrder.quantity = quantity.toInt()
        productOrder.shopId = shopItem.id!!
        productOrder.shopName = shopItem.name
        productOrder.shopSubtitle = "na"
        productOrder.shopImage = shopItem.logo
        productOrder.image = item.productImage
        productOrder.isChecked = false
        productOrder.isPopular = item.isPopular


        productOrder.variationId = item.variations[0].variationId
        productOrder.price = item.variations[0].price.mrp ?: 0.0
        try {
            productOrder.discountedPrice = item.variations[0].price.discountedPrice ?: 0.0

        } catch (e: Exception) {
            productOrder.discountedPrice = 0.0
        }

        successAddToCartData.postValue(
            JachaiFoodApplication.mDatabase.daoAccess().insertOrder(productOrder, isFromSameShop)
        )


    }

    fun checkCartStatus() {
        if (JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize() > 0) {
            successAddToCartData.postValue(true)
        } else {
            successAddToCartData.postValue(false)
        }
    }


}