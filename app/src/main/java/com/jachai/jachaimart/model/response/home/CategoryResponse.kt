package com.jachai.jachaimart.model.response.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryResponse(

    @field:SerializedName("categories")
    val categories: List<CategoriesItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
) : Parcelable

@Parcelize
data class CategoriesItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("parent")
    val parent: String? = null,

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
