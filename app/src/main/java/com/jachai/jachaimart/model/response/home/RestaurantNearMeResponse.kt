package com.jachai.jachaimart.model.response.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.product.Location
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantNearMeResponse(

	@field:SerializedName("last")
	val last: Boolean? = null,

	@field:SerializedName("numberOfElements")
	val numberOfElements: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPageNumber")
	val currentPageNumber: Int? = null,

	@field:SerializedName("shops")
	val shops: List<ShopsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("first")
	val first: Boolean? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("totalElements")
	val totalElements: Int? = null
) : Parcelable

@Parcelize
data class ShopsItem(

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("timeRemaining")
	val timeRemaining: Int? = null,

	@field:SerializedName("deliveryCharge")
	val deliveryCharge: Double? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("isFreeDelivery")
	val isFreeDelivery: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("numberOfRating")
	val numberOfRating: Int? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable



