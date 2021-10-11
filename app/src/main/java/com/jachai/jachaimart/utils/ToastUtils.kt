package com.jachai.jachai_driver.utils

import androidx.annotation.StringRes
import com.jachai.jachaimart.JachaiFoodApplication.Companion.getAppContext
import es.dmoral.toasty.Toasty

object ToastUtils {
    fun normal(message: String, isLong: Boolean = true) {
        Toasty.normal(
            getAppContext(),
            message,
            if (isLong) Toasty.LENGTH_LONG else Toasty.LENGTH_SHORT
        ).show()
    }

    fun normal(@StringRes resId: Int, isLong: Boolean = true) {
        normal(getAppContext().getText(resId).toString(), isLong)
    }

    fun warning(message: String) {
        Toasty.warning(getAppContext(), message, Toasty.LENGTH_LONG, true).show()
    }

    fun success(message: String) {
        Toasty.success(getAppContext(), message, Toasty.LENGTH_LONG, true).show()
    }

    fun success(@StringRes resId: Int) {
        success(getAppContext().getText(resId).toString())
    }

    fun error(message: String) {
        Toasty.error(getAppContext(), message, Toasty.LENGTH_LONG, true).show()
    }
}