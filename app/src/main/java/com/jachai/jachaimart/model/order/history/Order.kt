package com.jachai.jachaimart.model.order.history


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("deliveryMan")
    var deliveryMan: DeliveryMan,
    @SerializedName("deliveryManId")
    var deliveryManId: String,
    @SerializedName("deliveryManLocation")
    var deliveryManLocation: Any,
    @SerializedName("orderId")
    var orderId: String,
    @SerializedName("orderNote")
    var orderNote: String,
    @SerializedName("paymentMethod")
    var paymentMethod: String,
    @SerializedName("shippingLocation")
    var shippingLocation: ShippingLocation,
    @SerializedName("shop")
    var shop: Shop,
    @SerializedName("shopLocation")
    var shopLocation: ShopLocation,
    @SerializedName("status")
    var status: String,
    @SerializedName("statusOfDeliveryMan")
    var statusOfDeliveryMan: String,
    @SerializedName("statusOfMerchant")
    var statusOfMerchant: String,
    @SerializedName("subTotal")
    var subTotal: Int,
    @SerializedName("tokenNumber")
    var tokenNumber: Int,
    @SerializedName("total")
    var total: Double,
    @SerializedName("totalPaid")
    var totalPaid: Int
)