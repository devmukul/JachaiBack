package com.jachai.jachaimart.model.shop

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.jachai.jachaimart.database.DateConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
@Entity(tableName = "SearchHistory")
data class SearchHistoryItem(

    @field:SerializedName("searchKey")
    @PrimaryKey
    @ColumnInfo(defaultValue = "")
    val key: String = "",

    @field:SerializedName("updatedAtKey")
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    val updatedAt: Date = Date(System.currentTimeMillis())

) : Parcelable