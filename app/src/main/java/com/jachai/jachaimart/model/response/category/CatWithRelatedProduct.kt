package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName

data class CatWithRelatedProduct(
    @SerializedName("category")
    val category: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("products")
    val products: List<Product>
)