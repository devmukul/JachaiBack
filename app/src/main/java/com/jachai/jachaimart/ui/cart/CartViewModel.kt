package com.jachai.jachaimart.ui.cart

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.ui.base.BaseViewModel

class CartViewModel(application: Application) : BaseViewModel(application) {

    var successShopDetailsLiveData = MutableLiveData<ProductOrder>()
    var successProductOrderListLiveData = MutableLiveData<List<ProductOrder>>()


    fun getOrderDetails() {
        if (JachaiFoodApplication.mDatabase.daoAccess().getProductOrdersSize() > 0) {
            successShopDetailsLiveData.postValue(
                JachaiFoodApplication.mDatabase.daoAccess().getProductOrders()[0]
            )
        } else {

        }
    }

    fun updateQuantity(item: ProductOrder?) {
        if (item != null) {
            item.quantity?.let {

                if (it <= 0) {
                    JachaiFoodApplication.mDatabase.daoAccess()
                        .deleteCartProducts(listOf(item.product))


                } else {
                    JachaiFoodApplication.mDatabase.daoAccess()
                        .updateProductQuantity(it, item.product)

                }
                geOrderList()


            }

        }

    }

    fun geOrderList() {
        successProductOrderListLiveData.postValue(
            JachaiFoodApplication.mDatabase.daoAccess().getProductOrders()
        )
    }


}