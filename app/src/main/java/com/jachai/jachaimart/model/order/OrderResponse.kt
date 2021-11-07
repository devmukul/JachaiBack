package com.jachai.jachaimart.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.order.details.Product
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderResponse(

	@field:SerializedName("total")
	val total: Double? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null,

	@field:SerializedName("totalPaid")
	val totalPaid: Double? = null,

	@field:SerializedName("subtotal")
	val subtotal: Double? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("products")
	val products: List<Product?>? = null
) : Parcelable

