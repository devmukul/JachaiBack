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


    const val LOGIN_BASE = "user/login"
    //const val SIGNUP_BASE = "driver/register"
    const val OTP_REQUEST_BASE = "otp/send"
    const val NAME_UPDATE_REQUEST_BASE = "user/update-name"
    const val USER_INFO_REQUEST_BASE = "user"


    //banner
    const val BANNER_BASE = "banner"
    const val CATEGORY_BASE = "category/type"

    //shop
    const val SHOP_BASE = "shop"
    const val SHOP_NEAREST_BASE = "shop/nearest"
    const val SHOP_BY_CATEGORY_BASE = "shop/by-category"

    const val PRODUCT_BY_CAT_BASE = "product/product-category"

    const val CAR_REG_BASE = "car/info"
    const val DRIVER_DOC_STATUS_BASE = "driver/info"
    const val DRIVER_DOC_UPLOAD_BASE = "documents"

    // Credential Manager
    const val RC_SAVE = 1000
    const val RC_READ = 1001
    const val PHONE_PICKER_REQUEST = 1002

    const val CURRENT_TRIP_BASE = "driver/current-trip"


}