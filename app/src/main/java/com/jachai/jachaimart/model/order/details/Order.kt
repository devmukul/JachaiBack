package com.jachai.jachaimart.model.order.details


import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.order.history.DeliveryMan
import com.jachai.jachaimart.model.order.history.ShippingLocation
import com.jachai.jachaimart.model.order.history.Shop
import com.jachai.jachaimart.model.order.history.ShopLocation

data class Order(
    @SerializedName("alreadyCanceledDriver")
    var alreadyCanceledDriver: List<String>,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("customer")
    var customer: Customer,
    @SerializedName("deliveryCharge")
    var deliveryCharge: Int,
    @SerializedName("deliveryMan")
    var deliveryMan: DeliveryMan,
    @SerializedName("deliveryManId")
    var deliveryManId: String,
    @SerializedName("deliveryManLocation")
    var deliveryManLocation: Any,
    @SerializedName("discount")
    var discount: Double,
    @SerializedName("id")
    var id: String,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("orderNote")
    var orderNote: String,
    @SerializedName("orderedBy")
    var orderedBy: String,
    @SerializedName("paymentMethod")
    var paymentMethod: String,
    @SerializedName("products")
    var products: List<Product>,
    @SerializedName("promoCode")
    var promoCode: Any,
    @SerializedName("shippingAddress")
    var shippingAddress: String,
    @SerializedName("shippingLocation")
    var shippingLocation: ShippingLocation,
    @SerializedName("shop")
    var shop: Shop,
    @SerializedName("shopLocation")
    var shopLocation: ShopLocation,
    @SerializedName("status")
    var status: String,
    @SerializedName("statusLogs")
    var statusLogs: List<StatusLog>,
    @SerializedName("statusOfDeliveryMan")
    var statusOfDeliveryMan: String,
    @SerializedName("statusOfMerchant")
    var statusOfMerchant: String,
    @SerializedName("subTotal")
    var subTotal: Double,
    @SerializedName("tokenNumber")
    var tokenNumber: String,
    @SerializedName("total")
    var total: Double,
    @SerializedName("totalPaid")
    var totalPaid: Int,
    @SerializedName("type")
    var type: String,
    @SerializedName("vat")
    var vat: Double
)