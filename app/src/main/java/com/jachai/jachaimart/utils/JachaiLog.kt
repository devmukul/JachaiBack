@file:Suppress("unused")

package com.jachai.jachai_driver.utils

import android.util.Log
import android.util.Log.*
import com.jachai.jachaimart.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException


class JachaiLog private constructor() {
    companion object {

        private const val MAX_LOG_LENGTH = 4000

        fun v(tag: String, msg: String) = println(VERBOSE, tag, msg)

        fun v(tag: String, msg: String, tr: Throwable) = println(VERBOSE, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun d(`class`: Class<*>, msg: String) = d(`class`.canonicalName, msg)

        fun d(`class`: Class<*>, msg: String, tr: Throwable) =
            d(`class`.canonicalName, "$msg+\n+${getStackTraceString(tr)}")

        fun d(tag: String, msg: String) = println(DEBUG, tag, msg)

        fun d(tag: String, msg: String, tr: Throwable) = println(DEBUG, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun i(tag: String, msg: String) = println(INFO, tag, msg)

        fun i(tag: String, msg: String, tr: Throwable) = println(INFO, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun w(tag: String, msg: String) = println(WARN, tag, msg)

        fun w(tag: String, msg: String, tr: Throwable) = println(WARN, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun w(tag: String, tr: Throwable) = println(WARN, tag, getStackTraceString(tr))

        fun wtf(tag: String, msg: String) = println(WARN, tag, msg)

        fun wtf(tag: String, msg: String, tr: Throwable) = println(WARN, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun wtf(tag: String, tr: Throwable) = println(WARN, tag, getStackTraceString(tr))

        fun e(tag: String, msg: String) = println(ERROR, tag, msg)

        fun e(tag: String, msg: String, tr: Throwable) = println(ERROR, tag, "$msg+\n+${getStackTraceString(tr)}")

        fun e(`class`: Class<*>, msg: String) = e(`class`.canonicalName, msg)

        fun e(`class`: Class<*>, msg: String, tr: Throwable) =
            e(`class`.canonicalName, "$msg+\n+${getStackTraceString(tr)}")

        private fun getStackTraceString(tr: Throwable?): String {
            if (tr == null) {
                return ""
            }

            // This is to reduce the amount of log spew that apps do in the non-error
            // condition of the network being unavailable.
            var t = tr
            while (t != null) {
                if (t is UnknownHostException) {
                    return ""
                }
                t = t.cause
            }

            val sw = StringWriter()
            val pw = PrintWriter(sw)
            tr.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        private fun println(priority: Int, tag: String, message: String) =
            if (BuildConfig.AVAILABILITY_LOG_PRINT.toBoolean()) {
                var i = 0
                val length = message.length
                var result = -1
                while (i < length) {
                    var newline = message.indexOf('\n', i)
                    newline = if (newline != -1) newline else length
                    do {
                        val end = Math.min(newline, i + MAX_LOG_LENGTH)
                        result = Log.println(priority, smartTag(tag), message.substring(i, end))
                        i = end
                    } while (i < newline)
                    i++
                }
                result
            } else {
                -1
            }

        private fun smartTag(tag: String): String {
            val splitterString = tag.split("\\.".toRegex())
            val tagBuilder = StringBuilder()
            for (i in 0 until splitterString.size) {
                if (i != splitterString.size - 1) {
                    tagBuilder.append(splitterString[i][0]).append(".")
                } else {
                    tagBuilder.append(splitterString[i])
                }
            }
            return tagBuilder.toString()
        }
    }
}