package com.jachai.jachaimart.model.response.grocery


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.model.response.grocery.Location
import com.jachai.jachaimart.model.response.product.CreatedBy
import com.jachai.jachaimart.model.response.product.UpdatedBy
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hub(
    @SerializedName("baseDeliveryCharge")
    val deliveryCharge: Double,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("createdBy")
    var createdBy: CreatedBy? = null,
    @SerializedName("deleted")
    var deleted: Boolean = false,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val logo: String,
    @SerializedName("isFreeDelivery")
    var isFreeDelivery: Boolean? = false,
    @SerializedName("location")
    var location: Location,
    @SerializedName("minimumAmountForFreeDelivery")
    val minimumAmountForFreeDelivery: Float= 0F,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("updatedBy")
    var updatedBy: UpdatedBy? = null,
    @SerializedName("vat")
    val vat: Double
) : Parcelable