package com.jachai.jachaimart.model.response.category


import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.product.Product

data class CatWithRelatedProduct(
    @SerializedName("category")
    val category: String,
    @SerializedName("categoryImage")
    val categoryImage: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("products")
    val products: List<Product>
)