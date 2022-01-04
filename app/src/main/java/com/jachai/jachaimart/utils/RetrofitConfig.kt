package com.jachai.jachaimart.utils

import com.jachai.jachaimart.JachaiApplication
import com.jachai.jachaimart.api.service.*


object RetrofitConfig {
    val foodService = JachaiApplication.CATELOGREADER_RETROFIT.create(FoodService::class.java)
    val authService = JachaiApplication.AUTH_RETROFIT.create(AuthService::class.java)
    val orderService = JachaiApplication.ORDER_RETROFIT.create(OrderService::class.java)
    val groceryService =
        JachaiApplication.CATELOGREADER_RETROFIT.create(GroceryService::class.java)
    val driverService =
        JachaiApplication.DRIVER_RETROFIT.create(DriverService::class.java)
    val paymentService =
        JachaiApplication.PAYMENT_RETROFIT.create(PaymentService::class.java)
    val notificationsService =
        JachaiApplication.NOTIFICATIONS_RETROFIT.create(NotificationsService::class.java)
    val mapService =
        JachaiApplication.JACHAI_MAP_RETROFIT.create(JaChaiMapService::class.java)
}