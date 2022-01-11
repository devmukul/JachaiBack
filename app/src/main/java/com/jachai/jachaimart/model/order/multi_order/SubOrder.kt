package com.jachai.jachaimart.model.order.multi_order


import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.order.details.Customer
import com.jachai.jachaimart.model.order.details.Product
import com.jachai.jachaimart.model.order.details.StatusLog
import com.jachai.jachaimart.model.order.history.DeliveryMan
import com.jachai.jachaimart.model.order.history.ShippingLocation
import com.jachai.jachaimart.model.order.history.Shop

data class SubOrder(

    @SerializedName("baseOrderId")
    var baseOrderId: String = "",
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("customer")
    var customer: Customer? = null,
    @SerializedName("deliveryCharge")
    var deliveryCharge: Double = 0.0,
    @SerializedName("deliveryMan")
    var deliveryMan: DeliveryMan? = null,
    @SerializedName("deliveryManId")
    var deliveryManId: String = "",
    @SerializedName("deliveryManLocation")
    var deliveryManLocation: Any = Any(),
    @SerializedName("discount")
    var discount: Double = 0.0,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("merchantAmount")
    var merchantAmount: Double = 0.0,
    @SerializedName("orderId")
    var orderId: String = "",
    @SerializedName("orderNote")
    var orderNote: String = "",
    @SerializedName("orderedBy")
    var orderedBy: String = "",
    @SerializedName("paymentMethod")
    var paymentMethod: String = "",
    @SerializedName("products")
    var products: List<Product> = listOf(),
    @SerializedName("promoCode")
    var promoCode: Any = Any(),
    @SerializedName("salesManName")
    var salesManName: String ="",
    @SerializedName("shippingAddress")
    var shippingAddress: String = "",
    @SerializedName("shippingLocation")
    var shippingLocation: ShippingLocation = ShippingLocation(),
    @SerializedName("shop")
    var shop: Shop = Shop(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("statusLogs")
    var statusLogs: List<StatusLog> = listOf(),
    @SerializedName("statusOfDeliveryMan")
    var statusOfDeliveryMan: String = "",
    @SerializedName("statusOfMerchant")
    var statusOfMerchant: String = "",
    @SerializedName("subTotal")
    var subTotal: Double = 0.0,
    @SerializedName("tokenNumber")
    var tokenNumber: String = "",
    @SerializedName("total")
    var total: Double = 0.0,
    @SerializedName("totalPaid")
    var totalPaid: Double = 0.0,
    @SerializedName("type")
    var type: String = "",
    @SerializedName("vat")
    var vat: Double = 0.0
)