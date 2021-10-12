package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ProductX(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("isPopular")
    @Expose
    var isPopular: Boolean,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("numberOfRating")
    @Expose
    var numberOfRating: Int,
    @SerializedName("productImage")
    @Expose
    var productImage: String,
    @SerializedName("rating")
    @Expose
    var rating: Int,
    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("variations")
    @Expose
    var variations: List<Variation>
)