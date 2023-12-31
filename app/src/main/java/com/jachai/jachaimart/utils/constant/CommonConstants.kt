package com.jachai.jachaimart.utils.constant

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object CommonConstants {

    const val CUSTOMER_CARE_PHONE_NO = "+8801955529893"

    const val JC_FOOD_TYPE = "JC_FOOD"
    const val JC_MART_TYPE = "JC_MART"

    const val DEFAULT_TYPE = JC_MART_TYPE

    const val DATABASE_NAME = "JACHAI_CUSTOMER.db"


    const val POLICY_URL = "https://jachai.com/privacy-policy"
    val DEFAULT_GSON = GsonBuilder().serializeNulls().create()!!
    val HIDE_NON_EXPOSED_GSON = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!!
    val DEFAULT_NON_NULL_GSON = Gson()

    const val INVALID_TIMESTAMP = -1L
    const val INVALID_ACCESS_TOKEN = ""
    const val INVALID_ACCESS_TOKEN_WITHOUT_BEARER = ""
    const val INVALID_FIRE_BASE_TOKEN = ""

    const val TOKEN_MAX_ALIVE_TIME = 1000 * 60 * 60 * 24 * 15L
    const val CONNECTION_TIMEOUT = 2 * 60 * 1000L


    const val NOTIFICATION_TITLE = "title"
    const val NOTIFICATION_BODY = "message"
    const val NOTIFICATION_TYPE = "type"
    const val NOTIFICATION_TYPE_ID = "typeId"
    const val NOTIFICATION_IMAGE_URL = "image"
    const val NOTIFICATION_REDIRECT_URL = "redirect_url"
    const val NOTIFICATION_DATA = "notification_data"

    const val NOTIFICATION_CHANNEL = "Jachai_NOTIFICATION"
}