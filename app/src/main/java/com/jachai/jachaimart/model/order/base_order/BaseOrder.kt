package com.jachai.jachaimart.model.order.base_order


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "BaseOrders")
@Parcelize
data class BaseOrder(
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    @SerializedName("baseOrderId")
    var baseOrderId: String,

    @SerializedName("createdAt")
    @ColumnInfo(defaultValue = "")
    var createdAt: String = "",

    @SerializedName("hubName")
    @ColumnInfo(defaultValue = "")
    var hubName: String = "",

    @SerializedName("orderNote")
    @ColumnInfo(defaultValue = "")
    var orderNote: String = "",


    @SerializedName("paymentMethod")
    @ColumnInfo(defaultValue = "")
    var paymentMethod: String = "",

    @SerializedName("status")
    @ColumnInfo(defaultValue = "")
    var status: String = "",


    @SerializedName("subTotal")
    @ColumnInfo(defaultValue = "")
    var subTotal: Int = 0,


    @SerializedName("total")
    @ColumnInfo(defaultValue = "")
    var total: Int = 0,


    @SerializedName("totalPaid")
    @ColumnInfo(defaultValue = "")
    var totalPaid: Int = 0
) : Parcelable