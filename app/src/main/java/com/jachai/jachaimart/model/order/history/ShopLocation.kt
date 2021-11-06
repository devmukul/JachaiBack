package com.jachai.jachaimart.model.order.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ShopLocation")
class ShopLocation {
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    var shopId: String= ""

    @SerializedName("latitude")
    @ColumnInfo(defaultValue = "")
    var latitude: Double? = null

    @SerializedName("longitude")
    @ColumnInfo(defaultValue = "")
    var longitude: Double? = null
}