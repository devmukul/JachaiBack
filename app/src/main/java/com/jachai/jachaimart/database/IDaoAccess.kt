package com.jachai.jachaimart.database

import androidx.annotation.Keep
import androidx.room.*
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.request.FProductsItem

@Keep
@Dao
interface IDaoAccess {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductOrder(productOrder: ProductOrder)

    @Transaction
    fun insertOrder(productOrder: ProductOrder, isFromSameShop: Boolean): Boolean {
        if (isFromSameShop) {
            insertProductOrder(productOrder)
        } else {
            clearOrderTable()
            insertProductOrder(productOrder)
        }
        return true
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

    @Query("SELECT SUM(price * quantity) FROM ProductOrder")
    fun totalCost(): Double

    @Query("UPDATE ProductOrder SET quantity = :itemQty WHERE product = :id ;")
    fun updateProductQuantity(itemQty: Int, id: String)

    @Query("DELETE FROM ProductOrder where product in (:id)")
    fun deleteCartProducts(id: List<String>)

    @Query("SELECT SUM(price * quantity) FROM ProductOrder")
    fun getProductOrderSubtotal(): Double

    @Query("SELECT quantity FROM ProductOrder WHERE product =:id")
    fun getOrderCount(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteProduct(fProductsItem: List<FProductsItem>)


}