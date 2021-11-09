package com.jachai.jachaimart.model.order

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "ProductOrder")
class ProductOrder {

    @SerializedName("product")
    @Expose
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    var product: String = ""

    @ColumnInfo(defaultValue = "")
    var productName: String? = null

    @SerializedName("quantity")
    @Expose
    @ColumnInfo(defaultValue = "")
    var quantity: Int? = 0

    @SerializedName("variant")
    @Expose
    @ColumnInfo(defaultValue = "")
    var variant: String? = null

    @ColumnInfo(defaultValue = "")
    var shopId: String? = null

    @ColumnInfo(defaultValue = "")
    var shopName: String? = null

    @ColumnInfo(defaultValue = "")
    var shopSubtitle: String? = null

    @ColumnInfo(defaultValue = "")
    var shopImage: String? = null

    @ColumnInfo(defaultValue = "")
    var image: String? = null

    @ColumnInfo(defaultValue = "0")
    var price: Double? = null

    @ColumnInfo(defaultValue = "0")
    var discountedPrice: Double? = 0.0

    @ColumnInfo(defaultValue = "")
    var campaignName: String? = null


    @ColumnInfo(defaultValue = "")
    var variationId: String? = null

    @ColumnInfo(defaultValue = "")
    var isChecked: Boolean? = null

    @ColumnInfo(defaultValue = "")
    var isPopular: Boolean? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductOrder

        if (isChecked != other.isChecked) return false

        return true
    }
}