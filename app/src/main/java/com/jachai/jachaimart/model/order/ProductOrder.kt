package com.jachai.jachaimart.model.order

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.utils.constant.CommonConstants

@Keep
@Entity(tableName = "ProductOrder", primaryKeys = ["product", "variationId"])
class ProductOrder {

    var orderModule: String = CommonConstants.DEFAULT_TYPE


    @SerializedName("product")
    @Expose
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
    var shopId: String = ""


    @ColumnInfo(defaultValue = "")
    var hubId: String = ""

    @ColumnInfo(defaultValue = "")
    var hubName: String = ""

    @ColumnInfo(defaultValue = "")
    var hubImage: String = ""

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

    @SerializedName("maximumOrderLimit")
    @ColumnInfo(defaultValue = "")
    var maximumOrderLimit: Int = 0

    @SerializedName("stock")
    @ColumnInfo(defaultValue = "0")
    var stock: Int = 0

    @ColumnInfo(defaultValue = "")
    var variationId: String = ""

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