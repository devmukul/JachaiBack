package com.jachai.jachaimart.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetailsResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null,

    @field:SerializedName("order")
    val order: Order? = null
) : Parcelable

@Parcelize
data class ShippingLocation(

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null
) : Parcelable

@Parcelize
data class CreatedBy(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
) : Parcelable

@Parcelize
data class Price(

    @field:SerializedName("discountedPrice")
    val discountedPrice: Double? = null,

    @field:SerializedName("mrp")
    val mrp: Double? = null
) : Parcelable

@Parcelize
data class Order(

    @field:SerializedName("shopLocation")
    val shopLocation: ShopLocation? = null,

    @field:SerializedName("shop")
    val shop: Shop? = null,

    @field:SerializedName("orderId")
    val orderId: String? = null,

    @field:SerializedName("statusLogs")
    val statusLogs: List<StatusLogsItem?>? = null,

    @field:SerializedName("totalPaid")
    val totalPaid: Double? = null,

    @field:SerializedName("orderNote")
    val orderNote: String? = null,

    @field:SerializedName("deliveryManLocation")
    val deliveryManLocation: String? = null,

    @field:SerializedName("deliveryMan")
    val deliveryMan: String? = null,

    @field:SerializedName("deliveryCharge")
    val deliveryCharge: Int? = null,

    @field:SerializedName("subTotal")
    val subTotal: Double? = null,

    @field:SerializedName("deliveryManId")
    val deliveryManId: String? = null,

    @field:SerializedName("products")
    val products: List<ProductsItem?>? = null,

    @field:SerializedName("total")
    val total: Double? = null,

    @field:SerializedName("orderedBy")
    val orderedBy: String? = null,

    @field:SerializedName("alreadyCanceledDriver")
    val alreadyCanceledDriver: String? = null,

    @field:SerializedName("shippingLocation")
    val shippingLocation: ShippingLocation? = null,

    @field:SerializedName("paymentMethod")
    val paymentMethod: String? = null,

    @field:SerializedName("promoCode")
    val promoCode: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("customer")
    val customer: Customer? = null,

    @field:SerializedName("statusOfMerchant")
    val statusOfMerchant: String? = null,

    @field:SerializedName("statusOfDeliveryMan")
    val statusOfDeliveryMan: String? = null,

    @field:SerializedName("shippingAddress")
    val shippingAddress: String? = null,

	@field:SerializedName("vat")
	val vat: Double? = null,

	@field:SerializedName("discount")
	val discount: Double? = null,

	@field:SerializedName("tokenNumber")
	val tokenNumber: Int? = null

	) : Parcelable

@Parcelize
data class ShopLocation(

    @field:SerializedName("latitude")
    val latitude: Double? = null,

    @field:SerializedName("longitude")
    val longitude: Double? = null
) : Parcelable

@Parcelize
data class ProductsItem(

    @field:SerializedName("productImage")
    val productImage: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("variation")
    val variation: Variation? = null
) : Parcelable

@Parcelize
data class Shop(

    @field:SerializedName("area")
    val area: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("banner")
    val banner: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("ownerId")
    val ownerId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("contactNumber")
    val contactNumber: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class Customer(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
) : Parcelable

@Parcelize
data class ProductDiscount(

    @field:SerializedName("maxLimit")
    val maxLimit: Int? = null,

    @field:SerializedName("flat")
    val flat: Int? = null,

    @field:SerializedName("percentage")
    val percentage: Int? = null
) : Parcelable

@Parcelize
data class QuantitativeProductDiscount(

    @field:SerializedName("freeProduct")
    val freeProduct: Int? = null,

    @field:SerializedName("productDiscount")
    val productDiscount: ProductDiscount? = null,

    @field:SerializedName("minimumQuantity")
    val minimumQuantity: Int? = null
) : Parcelable

@Parcelize
data class Variation(

    @field:SerializedName("variationId")
    val variationId: String? = null,

    @field:SerializedName("variationName")
    val variationName: String? = null,

    @field:SerializedName("price")
    val price: Price? = null,

    @field:SerializedName("productDiscount")
    val productDiscount: ProductDiscount? = null,

    @field:SerializedName("stock")
    val stock: Int? = null,

    @field:SerializedName("quantitativeProductDiscount")
    val quantitativeProductDiscount: QuantitativeProductDiscount? = null
) : Parcelable

@Parcelize
data class StatusLogsItem(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("datetime")
    val datetime: String? = null,

    @field:SerializedName("createdBy")
    val createdBy: CreatedBy? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("value")
    val value: String? = null
) : Parcelable
