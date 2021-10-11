package com.jachai.jachaimart.utils

import com.jachai.jachai_driver.utils.JachaiLog
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {

    companion object {
        private val TAG = HttpLogger::class.java.canonicalName
    }

    override fun log(message: String) {
        if (message != null) {
            JachaiLog.i(TAG!!, message)
        }
    }
}