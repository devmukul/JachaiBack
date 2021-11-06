package com.jachai.jachaimart.model.order.history


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DeliveryMan")
class DeliveryMan {
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    var orderId: String= ""

    @SerializedName("id")
    @ColumnInfo(defaultValue = "")
    var id: String? = null

    @SerializedName("mobileNumber")
    @ColumnInfo(defaultValue = "")
    var mobileNumber: String? = null

    @SerializedName("name")
    @ColumnInfo(defaultValue = "")
    var name: String? = null
}