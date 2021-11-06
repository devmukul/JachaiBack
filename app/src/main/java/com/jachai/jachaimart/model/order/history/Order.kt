package com.jachai.jachaimart.model.order.history


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Order")
class Order {
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    @SerializedName("orderId")
    var orderId: String= ""

    @SerializedName("createdAt")
    @ColumnInfo(defaultValue = "")
    var createdAt: String? = null

    @SerializedName("deliveryMan")
    @Ignore
    var deliveryMan: DeliveryMan? = null

    @SerializedName("deliveryManId")
    @ColumnInfo(defaultValue = "")
    var deliveryManId: String? = null

    @SerializedName("deliveryManLocation")
    @ColumnInfo(defaultValue = "")
    var deliveryManLocation: String? = null

    @SerializedName("orderNote")
    @ColumnInfo(defaultValue = "")
    var orderNote: String? = null

    @SerializedName("paymentMethod")
    @ColumnInfo(defaultValue = "")
    var paymentMethod: String? = null

    @SerializedName("shippingLocation")
    @Ignore
    var shippingLocation: ShippingLocation? = null

    @SerializedName("shop")
    @Ignore
    var shop: Shop? = null

    @SerializedName("shopLocation")
    @Ignore
    var shopLocation: ShopLocation? = null

    @SerializedName("status")
    @ColumnInfo(defaultValue = "")
    var status: String? = null

    @SerializedName("statusOfDeliveryMan")
    @ColumnInfo(defaultValue = "")
    var statusOfDeliveryMan: String? = null

    @SerializedName("statusOfMerchant")
    @ColumnInfo(defaultValue = "")
    var statusOfMerchant: String? = null

    @SerializedName("subTotal")
    @ColumnInfo(defaultValue = "")
    var subTotal: Int? = null

    @SerializedName("tokenNumber")
    @ColumnInfo(defaultValue = "")
    var tokenNumber: Int? = null

    @SerializedName("total")
    @ColumnInfo(defaultValue = "")
    var total: Double? = null

    @SerializedName("totalPaid")
    @ColumnInfo(defaultValue = "")
    var totalPaid: Int? = null
}