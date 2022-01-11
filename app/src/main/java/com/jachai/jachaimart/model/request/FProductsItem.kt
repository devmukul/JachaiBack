package com.jachai.jachaimart.model.request

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "FavouriteProduct")
data class FProductsItem(

    @field:SerializedName("slug")
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    var productId: String = ""
) : Parcelable