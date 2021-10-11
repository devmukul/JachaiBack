package com.jachai.jachaimart.utils

import com.jachai.jachaimart.JachaiFoodApplication
import com.jachai.jachaimart.service.AuthService


object RetrofitConfig {
    val authService = JachaiFoodApplication.AUTH_RETROFIT.create(AuthService::class.java)

}