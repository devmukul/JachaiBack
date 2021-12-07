package com.jachai.jachaimart.ui.groceries.category_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jachai.jachai_driver.utils.isConnectionAvailable
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.response.home.ShopsItem
import com.jachai.jachaimart.model.response.product.CategoryDetailsResponse
import com.jachai.jachaimart.model.shop.ProductX
import com.jachai.jachaimart.ui.base.BaseViewModel
import com.jachai.jachaimart.utils.HttpStatusCode
import com.jachai.jachaimart.utils.RetrofitConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroceryCategoryDetailsViewModel (application: Application) : BaseViewModel(application) {
    private var categoryDetailsCall: Call<CategoryDetailsResponse>? = null
    private val groceryService = RetrofitConfig.groceryService
    var successResponseLiveData = MutableLiveData<CategoryDetailsResponse?>()
//    var successAddToCartData = MutableLiveData<Boolean?>()

    fun getCategoryDetailsDetails(shopId: String, categoryId: String) {
        if (categoryDetailsCall != null) {
            return
        } else if (!getApplication<JachaiApplication>().isConnectionAvailable()) {
            return
        }

        categoryDetailsCall = groceryService.getShopCategoriesDetails(shopId, categoryId)

        categoryDetailsCall?.enqueue(object : Callback<CategoryDetailsResponse> {
            override fun onResponse(
                call: Call<CategoryDetailsResponse>?,
                response: Response<CategoryDetailsResponse>?
            ) {
                categoryDetailsCall = null
                when (response?.code()) {
                    HttpStatusCode.HTTP_OK -> successResponseLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<CategoryDetailsResponse>?, t: Throwable?) {
            }
        })
    }

//    fun saveProduct(item: ProductX, quantity: Int, shopItem: ShopsItem, isFromSameShop: Boolean) {
//        val productOrder = ProductOrder()
//        productOrder.product = item.id
//        productOrder.productName = item.name
//        productOrder.quantity = quantity.toInt()
//        productOrder.shopId = shopItem.id!!
//        productOrder.shopName = shopItem.name
//        productOrder.shopSubtitle = "na"
//        productOrder.shopImage = shopItem.logo
//        productOrder.image = item.productImage
//        productOrder.isChecked = false
//        productOrder.isPopular = item.isPopular
//
//
//        productOrder.variationId = item.variations[0].variationId
//        productOrder.price = item.variations[0].price.mrp.toDouble()
//        try {
//            productOrder.discountedPrice = item.variations[0].price.discountedPrice
//
//        } catch (e: Exception) {
//            productOrder.discountedPrice = 0.0
//        }
//
//        successAddToCartData.postValue(
//            JachaiApplication.mDatabase.daoAccess().insertOrder(productOrder, isFromSameShop)
//        )
//
//
//    }

    fun checkCartStatus() {
        if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() > 0) {
            successAddToCartData.postValue(true)
        } else {
            successAddToCartData.postValue(false)
        }
    }


}