package com.jachai.jachaimart.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryWithProductRequest(

	@field:SerializedName("limit")
	val limit: Int? = 10,

	@field:SerializedName("categories")
	var categories: List<String?>? = null,

	@field:SerializedName("page")
	val page: Int? = 0,

	@field:SerializedName("shopId")
	var shopId: String? = null
) : Parcelable
