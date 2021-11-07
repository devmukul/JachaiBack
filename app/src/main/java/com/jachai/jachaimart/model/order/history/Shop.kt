package com.jachai.jachaimart.model.order.history


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Shop")
class Shop {
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    var orderId: String= ""

    @SerializedName("address")
    @ColumnInfo(defaultValue = "")
    var address: String? = null

    @SerializedName("area")
    @ColumnInfo(defaultValue = "")
    var area: String? = null

    @SerializedName("banner")
    @ColumnInfo(defaultValue = "")
    var banner: String? = null

    @SerializedName("baseDeliveryCharge")
    @ColumnInfo(defaultValue = "")
    var baseDeliveryCharge: Int? = null

    @SerializedName("city")
    @ColumnInfo(defaultValue = "")
    var city: String? = null

    @SerializedName("contactNumber")
    @ColumnInfo(defaultValue = "")
    var contactNumber: String? = null

    @SerializedName("country")
    @ColumnInfo(defaultValue = "")
    var country: String? = null

    @SerializedName("id")
    @ColumnInfo(defaultValue = "")
    var id: String? = null

    @SerializedName("isFreeDelivery")
    @ColumnInfo(defaultValue = "")
    var isFreeDelivery: Boolean = false

    @SerializedName("logo")
    @ColumnInfo(defaultValue = "")
    var logo: String? = null

    @SerializedName("mobileNumber")
    @ColumnInfo(defaultValue = "")
    var mobileNumber: String? = null

    @SerializedName("name")
    @ColumnInfo(defaultValue = "")
    var name: String? = null

    @SerializedName("ownerId")
    @ColumnInfo(defaultValue = "")
    var ownerId: String? = null

    @SerializedName("prepareTime")
    @ColumnInfo(defaultValue = "")
    var prepareTime: Int? = null

    @SerializedName("slug")
    @ColumnInfo(defaultValue = "")
    var slug: String? = null

    @SerializedName("status")
    @ColumnInfo(defaultValue = "")
    var status: String? = null

    @SerializedName("type")
    @ColumnInfo(defaultValue = "")
    var type: String? = null
}