package com.jachai.jachaimart.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.utils.constant.CommonConstants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderRequest(

	@field:SerializedName("orderNote")
	var orderNote: String? = null,

	@field:SerializedName("shippingLocation")
	var shippingLocation: ShippingLocation? = null,

	@field:SerializedName("shippingAddress")
	var shippingAddress: String? = null,

	@field:SerializedName("type")
	val type: String? = CommonConstants.JC_MART_TYPE,

	@field:SerializedName("products")
	var products: List<ProductsItem?>? = null
) : Parcelable

@Parcelize
data class ShippingLocation(

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
) : Parcelable

@Parcelize
data class ProductsItem(

	@field:SerializedName("quantity")
	var quantity: Int? = null,

	@field:SerializedName("variationId")
	var variationId: String? = null,

	@field:SerializedName("productId")
	var productId: String? = null
) : Parcelable
