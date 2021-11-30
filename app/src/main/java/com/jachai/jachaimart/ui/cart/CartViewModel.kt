package com.jachai.jachaimart.ui.cart

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.ui.base.BaseViewModel

class CartViewModel(application: Application) : BaseViewModel(application) {

    var successShopDetailsLiveData = MutableLiveData<ProductOrder>()
    var successProductOrderListLiveData = MutableLiveData<List<ProductOrder>>()


    fun getOrderDetails() {
        if (JachaiApplication.mDatabase.daoAccess().getProductOrdersSize() > 0) {
            successShopDetailsLiveData.postValue(
                JachaiApplication.mDatabase.daoAccess().getProductOrders()[0]
            )
        } else {

        }
    }

    fun updateQuantity(item: ProductOrder?) {
        if (item != null) {
            item.quantity?.let {

                if (it <= 0) {
                    JachaiApplication.mDatabase.daoAccess()
                        .deleteCartProducts(item.product, item.variationId)


                } else {
                    JachaiApplication.mDatabase.daoAccess()
                        .updateProductQuantity(it, item.product, item.variationId)

                }
                geOrderList()


            }

        }

    }

    fun geOrderList() {
        successProductOrderListLiveData.postValue(
            JachaiApplication.mDatabase.daoAccess().getProductOrders()
        )
    }


}