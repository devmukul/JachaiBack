package com.jachai.jachaimart.database

import androidx.annotation.Keep
import androidx.room.*
import com.jachai.jachaimart.model.order.ProductOrder
import com.jachai.jachaimart.model.order.base_order.BaseOrder
import com.jachai.jachaimart.model.order.history.DeliveryMan
import com.jachai.jachaimart.model.order.history.Order
import com.jachai.jachaimart.model.order.history.ShippingLocation
import com.jachai.jachaimart.model.order.history.Shop
import com.jachai.jachaimart.model.request.FProductsItem
import com.jachai.jachaimart.model.shop.SearchHistoryItem

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

    @Query("DELETE FROM `Order`")
    fun clearLiveOrderTable()

    @Query("DELETE FROM `BaseOrders`")
    fun clearLiveBaseOrderTable()


    @Query("SELECT * FROM ProductOrder")
    fun getProductOrders(): List<ProductOrder>


    @Query("SELECT count(*) FROM ProductOrder WHERE shopId = :shopID")
    fun getProductByShopID(shopID: String): Int

    @Query("SELECT count(*) FROM ProductOrder WHERE hubId = :hubId")
    fun getProductByHubID(hubId: String): Int

    @Query("SELECT count(*) FROM ProductOrder WHERE product = :productId AND shopId = :shopID")
    fun getProductByProductID(productId: String, shopID: String): Int

    @Query("SELECT count(*) FROM ProductOrder WHERE product = :productId AND hubId = :hubId")
    fun getProductByProductIDByHub(productId: String, hubId: String): Int



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

    @Transaction
    fun isInsertionApplicableByHubId(hubId: String): Boolean {
        var isSameHubID = false
        isSameHubID = if (getProductOrdersSize() > 0) {
            getProductByHubID(hubId) > 0
        } else {
            true
        }
        return isSameHubID
    }

    @Query("SELECT SUM(price * quantity) FROM ProductOrder")
    fun totalCost(): Double

    @Query("UPDATE ProductOrder SET quantity = :itemQty WHERE product = :id AND variationId = :variationId ;")
    fun updateProductQuantity(itemQty: Int, id: String, variationId: String)

    @Query("DELETE FROM ProductOrder where product=:id AND variationId=:variationId")
    fun deleteCartProducts(id: String, variationId: String)

    @Query("SELECT SUM(price * quantity) FROM ProductOrder")
    fun getProductOrderSubtotal(): Double

    @Query("SELECT SUM(discountedPrice * quantity) FROM ProductOrder")
    fun getProductOrderDiscountedSubtotal(): Double

    @Query("SELECT quantity FROM ProductOrder WHERE product =:id")
    fun getOrderCount(id: String): Int

    @Query("SELECT SUM(quantity) FROM ProductOrder WHERE product =:id")
    fun getProductOrderCount(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteProduct(fProductsItem: List<FProductsItem>)

    @Query("DELETE FROM FavouriteProduct WHERE productId =:id")
    fun deleteFavouriteProduct(id: String)


    @Query("SELECT * FROM FavouriteProduct WHERE productId =:id")
    fun getFavouriteProductById(id: String): List<FProductsItem>


    @Query("SELECT * FROM FavouriteProduct")
    fun getAllFavouriteProducts(): List<FProductsItem>


    @Transaction
    fun insertOngoingOrder(order: Order): Boolean {
        insertOrder(order)
        order.deliveryMan?.let {
            order.deliveryMan!!.orderId = order.orderId
            insertDeliveryMain(it)

        }
        order.shop?.let {
            order.shop!!.orderId = order.orderId
            insertShop(it)
        }
        order.shippingLocation?.let {
            order.shippingLocation!!.orderId = order.orderId
            insertShippingLocation(it)
        }
        return true
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBaseOrder(baseOrder: BaseOrder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDeliveryMain(deliveryMan: DeliveryMan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShop(shop: Shop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShippingLocation(shippingLocation: ShippingLocation)

    @Query("SELECT COUNT(*) FROM `BaseOrders`")
    fun getOnGoingOrderCount(): Int

    @Transaction
    fun getLastOnGoingOrder(): Order {
        val order = getLastOrder()
        order.deliveryMan = getLastOrderDeliveryMan(orderId = order.orderId)
        order.shippingLocation = getLastOrderShippingLocation(orderId = order.orderId)
        order.shop = getLastOrderShop(orderId = order.orderId)

        return order
    }


    @Query("SELECT * FROM `Order` LIMIT 1")
    fun getLastOrder(): Order

    @Query("SELECT * FROM `BaseOrders` LIMIT 1")
    fun getLastBaseOrder(): BaseOrder

    @Query("SELECT * FROM Shop WHERE orderId= :orderId")
    fun getLastOrderShop(orderId: String): Shop

    @Query("SELECT * FROM ShippingLocation WHERE orderId= :orderId")
    fun getLastOrderShippingLocation(orderId: String): ShippingLocation

    @Query("SELECT * FROM DeliveryMan WHERE orderId= :orderId")
    fun getLastOrderDeliveryMan(orderId: String): DeliveryMan

    @Transaction
    fun geDiscountPrice(): Double {
        return getProductOrderDiscountedSubtotal() - getProductOrderSubtotal()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchKeyword(items: SearchHistoryItem)

    @Query("SELECT * FROM SearchHistory  ORDER BY updated_at DESC")
    fun getSearchHistory(): List<SearchHistoryItem>


}