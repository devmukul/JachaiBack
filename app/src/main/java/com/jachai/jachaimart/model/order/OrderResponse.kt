package com.jachai.jachaimart.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null,

	@field:SerializedName("totalPaid")
	val totalPaid: Int? = null,

	@field:SerializedName("subtotal")
	val subtotal: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null
) : Parcelable

