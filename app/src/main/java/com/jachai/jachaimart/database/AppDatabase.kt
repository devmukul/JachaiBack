package com.jachai.jachaimart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.order.base_order.BaseOrder
import com.jachai.jachaimart.model.order.history.DeliveryMan
import com.jachai.jachaimart.model.order.history.Order
import com.jachai.jachaimart.model.order.history.ShippingLocation
import com.jachai.jachaimart.model.order.history.Shop
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.shop.SearchHistoryItem
import com.jachai.jachaimart.utils.Converters
import com.jachai.jachaimart.utils.constant.CommonConstants.DATABASE_NAME


@Database(
    entities = [
        ProductOrder::class,
        FProductsItem::class,
        Order::class,
        DeliveryMan::class,
        ShippingLocation::class,
        Shop::class,
        SearchHistoryItem::class,
        BaseOrder::class

    ],
    version = 35,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoAccess(): IDaoAccess

    companion object {
        private var instance: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}