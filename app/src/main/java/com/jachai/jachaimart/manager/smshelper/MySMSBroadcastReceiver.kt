package com.jachai.jachai_driver.manager.smshelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern


class MySMSBroadcastReceiver : BroadcastReceiver() {

    private var otpReceiver: OTPReceiveListener? = null

    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Log.e("OtpMessage", "otp: $message")
                    retrieveOtpFromMessage(message)
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }
        }
    }

    private fun retrieveOtpFromMessage(message: String?) {
        if (message != null) {
            val pattern = Pattern.compile("(\\d{5})")
            //   \d is for a digit
            //   {} is the number of digits here 5.
            val matcher = pattern.matcher(message)
            var otp = ""
            if (matcher.find()) {
                otp = matcher.group(0) // 5 digit number
                Log.e("retrieveOtp", "myotp: $otp")
                otpReceiver?.onOTPReceived(otp)
            }
        }
    }

    interface OTPReceiveListener {

        fun onOTPReceived(otp: String)
    }
}