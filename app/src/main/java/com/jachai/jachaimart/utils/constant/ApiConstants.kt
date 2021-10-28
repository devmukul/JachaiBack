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
    const val JACHAI_BASE_URL_CATELOGREADER = BuildConfig.BASE_URL_JACHAI_CATELOGREADER
    const val JACHAI_BASE_URL_ORDER = BuildConfig.BASE_URL_JACHAI_ORDER
//    const val JACHAI_BASE_URL_DRIVER = BuildConfig.BASE_URL_JACHAI_DRIVER
//    const val JACHAI_BASE_URL_WEBSOCKET = BuildConfig.BASE_URL_JACHAI_WEBSOCKET
//    const val JACHAI_MAP_KEY = BuildConfig.BASE_URL_JACHAI_MAPKEY
//    const val JACHAI_BASE_URL_FARE = BuildConfig.BASE_URL_JACHAI_FARE


    const val LOGIN_BASE = "user/login"

    //const val SIGNUP_BASE = "driver/register"
    const val OTP_REQUEST_BASE = "otp/send"
    const val NAME_UPDATE_REQUEST_BASE = "user/update-name"
    const val USER_INFO_REQUEST_BASE = "user"


    //orders
    const val ORDER_REQUEST_BASE = "order"
    const val ORDER_DETAILS_BASE = "order/details-by-user"


    //banner
    const val BANNER_BASE = "banner"
    const val CATEGORY_BASE = "category/type"

    //shop
    const val SHOP_BASE = "shop"
    const val SHOP_NEAREST_BASE = "shop/nearest"
    const val SHOP_BY_CATEGORY_BASE = "shop/by-category"

    const val SHOP_CATEGORIES_BASE = "shop/categories"


    //product
    const val PRODUCT_BY_CAT_BASE = "product/product-category"
    const val PRODUCT_DETAILS_BASE = "product/details"


    const val CAR_REG_BASE = "car/info"
    const val DRIVER_DOC_STATUS_BASE = "driver/info"
    const val DRIVER_DOC_UPLOAD_BASE = "documents"

    // Credential Manager
    const val RC_SAVE = 1000
    const val RC_READ = 1001
    const val PHONE_PICKER_REQUEST = 1002

    const val CURRENT_TRIP_BASE = "driver/current-trip"


    //status
    const val ORDER_INITIATED = "INITIATED"
    const val ORDER_ACCEPTED_BY_DELIVERY_MAN = "ACCEPTED_BY_DELIVERY_MAN"
    const val ORDER_PROCESSING = "PROCESSING"
    const val ORDER_READY_TO_PICKUP = "READY_TO_PICKUP"
    const val ORDER_PICKED = "PICKED"
    const val ORDER_DELIVERED = "DELIVERED"
    const val ORDER_COMPLETED = "COMPLETED"


}