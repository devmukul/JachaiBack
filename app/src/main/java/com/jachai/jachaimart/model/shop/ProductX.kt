package com.jachai.jachaimart.model.shop


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.jachai.jachaimart.model.response.grocery.Hub
import com.jachai.jachaimart.model.response.product.*

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

    @SerializedName("slug")
    @Expose
    var slug: String,
    @SerializedName("type")
    @Expose
    var type: String,

    //    @field:SerializedName("shop")
//    val shop: Shop? = null,

    @field:SerializedName("hub")
    val hub: Hub? = null,

    @field:SerializedName("campaignId")
    val campaignId: String? = null,

    @field:SerializedName("hubId")
    val hubId: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("barCode")
    val barCode: String? = null,

    @field:SerializedName("urlCustomize")
    val urlCustomize: String? = null,

    @field:SerializedName("visitors")
    val visitors: Int? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("reviews")
    val reviews: String? = null,

    @field:SerializedName("variations")
    val variations: List<VariationsItem?>? = null,


    @field:SerializedName("campaign")
    val campaign: Campaign? = null,

    @field:SerializedName("location")
    val location: Location? = null,


    @field:SerializedName("shopId")
    val shopId: String? = null,

    @field:SerializedName("keyword")
    val keyword: String? = null,

    @field:SerializedName("sku")
    val sku: String? = null,

    @field:SerializedName("category")
    val category: Category? = null,

    @field:SerializedName("brand")
    val brand: Brand? = null,

    @field:SerializedName("categoryId")
    val categoryId: String? = null
)