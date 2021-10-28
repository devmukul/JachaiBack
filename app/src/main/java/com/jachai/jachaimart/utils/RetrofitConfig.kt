package com.jachai.jachaimart.utils

import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.api.service.AuthService
import com.jachai.jachaimart.api.service.FoodService
import com.jachai.jachaimart.api.service.GroceryService
import com.jachai.jachaimart.api.service.OrderService


object RetrofitConfig {
    val foodService = JachaiFoodApplication.CATELOGREADER_RETROFIT.create(FoodService::class.java)
    val authService = JachaiFoodApplication.AUTH_RETROFIT.create(AuthService::class.java)
    val orderService = JachaiFoodApplication.ORDER_RETROFIT.create(OrderService::class.java)
    val groceryService =
        JachaiFoodApplication.CATELOGREADER_RETROFIT.create(GroceryService::class.java)

}