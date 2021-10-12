package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Product(
    @SerializedName("category")
    @Expose
    var category: String,
    @SerializedName("products")
    @Expose
    var products: List<ProductX>
)