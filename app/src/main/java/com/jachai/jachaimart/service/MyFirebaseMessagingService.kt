package com.jachai.jachaimart.service

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bd.com.evaly.ehealth.models.notification.NotificationResponse
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.jachai.jachai_driver.utils.JachaiLog
import com.jachai.jachai_driver.utils.getDeviceId
import com.jachai.jachaimart.model.notification.NotificationRegisterRequest
import com.jachai.jachaimart.model.response.GenericResponse
import com.jachai.jachaimart.ui.home.HomeViewModel
import com.jachai.jachaimart.utils.RetrofitConfig
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.sendNotification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var broadcaster: LocalBroadcastManager? = null
    private val notificationsService = RetrofitConfig.notificationsService
    private var registerCall: Call<GenericResponse>? = null

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.e("Remote Message Data: ", Gson().toJson(remoteMessage.data))
            Log.e("Remote Message Intent:", Gson().toJson(remoteMessage.toIntent().extras))
            Log.e("Remote Notification:", Gson().toJson(remoteMessage.notification))
            if (remoteMessage.data.isNotEmpty()) {
                val notificationResponse = NotificationResponse()
                notificationResponse.title = remoteMessage.notification!!.title
                notificationResponse.body = remoteMessage.notification!!.body
                notificationResponse.type = remoteMessage.data[CommonConstants.NOTIFICATION_TYPE]
                notificationResponse.typeId =
                    remoteMessage.data[CommonConstants.NOTIFICATION_TYPE_ID]
                notificationResponse.image =
                    remoteMessage.data[CommonConstants.NOTIFICATION_IMAGE_URL]
                notificationResponse.redirectUrl =
                    remoteMessage.data[CommonConstants.NOTIFICATION_REDIRECT_URL]
                /*
                Sending data through boardcast receiver.
                 */
                val intent = Intent(CommonConstants.NOTIFICATION_DATA).apply {
                    putExtra(CommonConstants.NOTIFICATION_DATA, notificationResponse)
                }
                broadcaster!!.sendBroadcast(intent)
                sendNotification(notificationResponse, this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(token: String) {
        requestRegister(token)
    }

    private fun requestRegister(fcmToken: String) {
        try {
            if (registerCall != null) {
                return
            }
            val notificationRegisterRequest = NotificationRegisterRequest(this.getDeviceId(), "Android", "${Build.BRAND}_${Build.MODEL}", fcmToken)

            registerCall = notificationsService.registerDevice(notificationRegisterRequest)

            registerCall?.enqueue(object : Callback<GenericResponse> {
                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    registerCall = null
                    JachaiLog.d(HomeViewModel.TAG, response.body().toString())
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    registerCall = null

                }
            })

        } catch (ex: Exception) {
            JachaiLog.d(HomeViewModel.TAG, ex.message!!)
        }

    }


}