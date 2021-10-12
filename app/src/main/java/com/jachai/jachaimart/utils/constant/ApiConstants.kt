package com.jachai.jachaimart.utils.constant

import com.jachai.jachaimart.BuildConfig


object ApiConstants {
    const val USER_AGENT_HEADER = "User-Agent"
    const val ACCEPT_HEADER = "Accept"
    const val CONTENT_TYPE_HEADER = "Content-Type"

    const val USER_AGENT_MOBILE_ANDROID = "mobile-android"
    const val ACCEPT_HEADER_JSON_UTF_8 = "application/json;charset=UTF-8"
    const val CONTENT_TYPE_JSON_UTF_8 = "application/json;charset=UTF-8"
    const val TOKEN = "token"

    const val AUTHORIZATION_KEY = "Authorization"

    //const val JACHAI_BASE_URL = BuildConfig.BASE_URL_IPAY
    const val JACHAI_BASE_URL_AUTH = BuildConfig.BASE_URL_JACHAI_AUTH
    const val JACHAI_BASE_URL_FOOD = BuildConfig.BASE_URL_JACHAI_FOOD
//    const val JACHAI_BASE_URL_DRIVER = BuildConfig.BASE_URL_JACHAI_DRIVER
//    const val JACHAI_BASE_URL_WEBSOCKET = BuildConfig.BASE_URL_JACHAI_WEBSOCKET
//    const val JACHAI_MAP_KEY = BuildConfig.BASE_URL_JACHAI_MAPKEY
//    const val JACHAI_BASE_URL_FARE = BuildConfig.BASE_URL_JACHAI_FARE


    const val LOGIN_BASE = "driver/login"

    //banner
    const val BANNER_BASE = "banner"
    const val CATEGORY_BASE = "category/type"

    //shop
    const val SHOP_BASE = "shop"
    const val SHOP_NEAREST_BASE = "shop/nearest"
    const val SHOP_BY_CATEGORY_BASE = "shop/by-category"


    // Credential Manager
    const val RC_SAVE = 1000
    const val RC_READ = 1001
    const val PHONE_PICKER_REQUEST = 1002

    const val CURRENT_TRIP_BASE = "driver/current-trip"


}