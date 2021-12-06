package com.jachai.jachaimart.model.response.product

import com.google.gson.annotations.SerializedName


data class ProductBySlugResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null,

    @field:SerializedName("products")
    val products: List<Product?>? = null
)


