package com.jachai.jachaimart.model.response.product


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.model.response.category.CatWithRelatedProduct

data class CategoryDetailsResponse(
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("productCategories")
    @Expose
    var productCategories: List<CatWithRelatedProduct>,
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int
)