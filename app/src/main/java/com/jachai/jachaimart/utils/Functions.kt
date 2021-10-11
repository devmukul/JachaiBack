package com.jachai.jachai_driver.utils


import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.showShortToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.showShortToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Fragment.showShortToast(resId: Int) = activity?.showShortToast(resId)

fun Fragment.showShortToast(message: String) = activity?.showShortToast(message)

fun Context.showLongToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

fun Context.showLongToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Fragment.showLongToast(resId: Int) = activity?.showLongToast(resId)

fun Fragment.showLongToast(message: String) = activity?.showLongToast(message)

fun String.isValidEmailAddress() = this.matches("(.+)@(.+\\.)?(.+)".toRegex())
fun String.isValidMobileNumber() = this.replace("[\\d()\\s.+\\-]".toRegex(), "").isEmpty()

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Context.isConnectionAvailable(): Boolean {
    val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Fragment.isConnectionAvailable() = activity?.isConnectionAvailable()!!

@SuppressLint("HardwareIds")
fun Context.getDeviceId() =
    (Build.MODEL + "-" + Settings.Secure.getString(
        contentResolver,
        Settings.Secure.ANDROID_ID
    )).replace(Regex("\\s+"), "-")

fun Long.format(minLength: Int): String {
    return if (this.toString().length < minLength) {
        val leadingZero = minLength - this.toString().length
        var temp = ""
        for (i in 0 until leadingZero) {
            temp += "0"
        }
        temp += this
        temp
    } else {
        this.toString()
    }
}

