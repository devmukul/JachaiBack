package com.jachai.jachaimart.utils

import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.api.service.FoodService


object RetrofitConfig {
    val foodService = JachaiFoodApplication.FOOD_RETROFIT.create(FoodService::class.java)

}