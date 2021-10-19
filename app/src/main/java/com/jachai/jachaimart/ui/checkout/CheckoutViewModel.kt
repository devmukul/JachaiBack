package com.jachai.jachaimart.ui.checkout

import android.app.Application
import bd.com.evaly.ehealth.models.common.CurrentLocation
import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.request.OrderRequest
import com.jachai.jachaimart.model.request.ProductsItem
import com.jachai.jachaimart.model.request.ShippingLocation
import com.jachai.jachaimart.ui.base.BaseViewModel

class CheckoutViewModel(application: Application) : BaseViewModel(application) {

    val db = JachaiFoodApplication.mDatabase.daoAccess()


    fun placeOrder(additionalComment: String, userLocation: CurrentLocation) {
        var productOrderList: List<ProductOrder> = db.getProductOrders()

        var orderRequest =  OrderRequest()
        val products = listOf<ProductsItem>()

        for (item in productOrderList){
            var productsItem = ProductsItem()
            productsItem.productId = item.product
            productsItem.quantity = item.quantity
            productsItem.variationId = item.variationId
            products.toMutableList().add(productsItem)
        }

        orderRequest.products = products
        orderRequest.orderNote = additionalComment

        orderRequest.shippingAddress =  userLocation.address
        orderRequest.shippingLocation = ShippingLocation(userLocation.latitude,userLocation.longitude)




    }

}