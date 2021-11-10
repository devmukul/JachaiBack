package com.jachai.jachaimart.model.shop

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "SearchHistory")
data class SearchHistoryItem(

    @field:SerializedName("searchKey")
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    val key: String = ""
) : Parcelable