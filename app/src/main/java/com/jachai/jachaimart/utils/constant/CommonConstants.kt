package com.jachai.jachaimart.utils.constant

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object CommonConstants {

    const val DEFAULT_TYPE = "JC_FOOD"
    const val JC_MART_TYPE = "JC_MART"
    const val DATABASE_NAME = "JC_FOOD.db"
    val DEFAULT_GSON = GsonBuilder().serializeNulls().create()!!
    val HIDE_NON_EXPOSED_GSON = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!!
    val DEFAULT_NON_NULL_GSON = Gson()

    const val INVALID_TIMESTAMP = -1L
    const val INVALID_ACCESS_TOKEN = ""
    const val INVALID_ACCESS_TOKEN_WITHOUT_BEARER =
        "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MTUxODQ3NWQyNmZmNDAwMDY1YjUzOTMiLCJpc3MiOiJBVVRIIiwibW9iaWxlTnVtYmVyIjoiKzg4MDE3NjgxNDkxMjEiLCJuYW1lIjoiTWFoYnViIEhhc2FuIiwiZXhwIjoxNjM1MzI3OTQwfQ.5I992S1GoKKAnub7Ao7hXUgkDxR3bX1H9v_UaNBPaic"
    const val INVALID_FIRE_BASE_TOKEN = ""

    const val TOKEN_MAX_ALIVE_TIME = 1000 * 60 * 60 * 24 * 15L
    const val CONNECTION_TIMEOUT = 2 * 60 * 1000L
}