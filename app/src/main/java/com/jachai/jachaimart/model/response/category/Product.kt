package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("isPopular")
    val isPopular: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("numberOfRating")
    val numberOfRating: Int,
    @SerializedName("productImage")
    val productImage: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("variations")
    val variations: List<Variation>
)