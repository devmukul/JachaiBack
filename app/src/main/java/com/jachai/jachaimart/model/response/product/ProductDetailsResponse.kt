package com.jachai.jachaimart.model.response.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailsResponse(

    @field:SerializedName("product")
    val product: Product? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
) : Parcelable

@Parcelize
data class Product(

    @field:SerializedName("shop")
    val shop: Shop? = null,

    @field:SerializedName("campaignId")
    val campaignId: String? = null,

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

    @field:SerializedName("productImage")
    val productImage: String? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("reviews")
    val reviews: String? = null,

    @field:SerializedName("variations")
    val variations: List<VariationsItem?>? = null,

    @field:SerializedName("brandId")
    val brandId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("numberOfRating")
    val numberOfRating: Int? = null,

    @field:SerializedName("campaign")
    val campaign: Campaign? = null,

    @field:SerializedName("location")
    val location: Location? = null,

    @field:SerializedName("id")
    val id: String? = null,

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

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("categoryId")
    val categoryId: String? = null
) : Parcelable

@Parcelize
data class Campaign(

    @field:SerializedName("parentCampaign")
    val parentCampaign: String? = null,

    @field:SerializedName("updatedBy")
    val updatedBy: UpdatedBy? = null,

    @field:SerializedName("displayOrder")
    val displayOrder: Int? = null,

    @field:SerializedName("banner")
    val banner: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("endAt")
    val endAt: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("createdBy")
    val createdBy: CreatedBy? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("colorCode")
    val colorCode: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("shopId")
    val shopId: String? = null,

    @field:SerializedName("startAt")
    val startAt: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class BankInfo(

    @field:SerializedName("routingNumber")
    val routingNumber: String? = null,

    @field:SerializedName("accountName")
    val accountName: String? = null,

    @field:SerializedName("branchName")
    val branchName: String? = null,

    @field:SerializedName("swiftCode")
    val swiftCode: String? = null,

    @field:SerializedName("bankName")
    val bankName: String? = null,

    @field:SerializedName("accountNumber")
    val accountNumber: String? = null
) : Parcelable

@Parcelize
data class Shop(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("keywords")
    val keywords: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("ownerId")
    val ownerId: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("contactNumber")
    val contactNumber: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("area")
    val area: String? = null,

    @field:SerializedName("bankInfo")
    val bankInfo: BankInfo? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("updatedBy")
    val updatedBy: UpdatedBy? = null,

    @field:SerializedName("banner")
    val banner: String? = null,

    @field:SerializedName("baseDeliveryCharge")
    val baseDeliveryCharge: Int? = null,

    @field:SerializedName("isFreeDelivery")
    val isFreeDelivery: Boolean? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("createdBy")
    val createdBy: CreatedBy? = null,

    @field:SerializedName("prepareTime")
    val prepareTime: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("numberOfRating")
    val numberOfRating: Int? = null,

    @field:SerializedName("location")
    val location: Location? = null
) : Parcelable

@Parcelize
data class VariationsItem(

    @field:SerializedName("variationId")
    val variationId: String? = null,

    @field:SerializedName("variationName")
    val variationName: String? = null,

    @field:SerializedName("price")
    val price: Price? = null,

    @field:SerializedName("productDiscount")
    val productDiscount: ProductDiscount? = null,

    @field:SerializedName("stock")
    val stock: Int? = null,

    @field:SerializedName("quantitativeProductDiscount")
    val quantitativeProductDiscount: QuantitativeProductDiscount? = null
) : Parcelable

@Parcelize
data class Location(

    @field:SerializedName("x")
    val X: Double? = null,

    @field:SerializedName("coordinates")
    val coordinates: List<Double?>? = null,

    @field:SerializedName("y")
    val Y: Double? = null,

    @field:SerializedName("type")
    val type: String? = null
) : Parcelable

@Parcelize
data class ProductDiscount(

    @field:SerializedName("flat")
    val flat: Int? = null,

    @field:SerializedName("percentage")
    val percentage: Int? = null
) : Parcelable

@Parcelize
data class Price(

    @field:SerializedName("discountedPrice")
    val discountedPrice: Int? = null,

    @field:SerializedName("mrp")
    val mrp: Int? = null
) : Parcelable

@Parcelize
data class UpdatedBy(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
) : Parcelable

@Parcelize
data class Category(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("parent")
    val parent: String? = null,

    @field:SerializedName("updatedBy")
    val updatedBy: UpdatedBy? = null,

    @field:SerializedName("level")
    val level: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("createdBy")
    val createdBy: CreatedBy? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("position")
    val position: Int? = null,

    @field:SerializedName("keyword")
    val keyword: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable

@Parcelize
data class CreatedBy(

    @field:SerializedName("mobileNumber")
    val mobileNumber: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
) : Parcelable

@Parcelize
data class Brand(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("deleted")
    val deleted: Boolean? = null,

    @field:SerializedName("updatedBy")
    val updatedBy: UpdatedBy? = null,

    @field:SerializedName("createdBy")
    val createdBy: CreatedBy? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable

@Parcelize
data class QuantitativeProductDiscount(

    @field:SerializedName("freeProduct")
    val freeProduct: String? = null,

    @field:SerializedName("productDiscount")
    val productDiscount: ProductDiscount? = null,

    @field:SerializedName("minimumQuantity")
    val minimumQuantity: Int? = null
) : Parcelable
