package com.jachai.jachaimart.database

import androidx.annotation.Keep
import androidx.room.*
import com.jachai.jachaimart.model.order.ProductOrder

@Keep
@Dao
interface IDaoAccess {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductOrder(productOrder: ProductOrder)

    @Transaction
    fun insertOrder(productOrder: ProductOrder, isFromSameShop: Boolean) {
        if (isFromSameShop) {
            insertProductOrder(productOrder)
        } else {
            clearOrderTable()
            insertProductOrder(productOrder)
        }
    }

    @Query("DELETE FROM ProductOrder")
    fun clearOrderTable()


    @Query("SELECT * FROM ProductOrder")
    fun getProductOrders(): List<ProductOrder>


    @Query("SELECT count(*) FROM ProductOrder WHERE shopId = :shopID")
    fun getProductByShopID(shopID: String): Int


    @Query("SELECT count(*) FROM ProductOrder")
    fun getProductOrdersSize(): Int

    @Transaction
    fun isInsertionApplicable(shopID: String): Boolean {
        var isSameShopID = false
        isSameShopID = if (getProductOrdersSize() > 0) {
            getProductByShopID(shopID) > 0
        } else {
            true
        }
        return isSameShopID
    }

}