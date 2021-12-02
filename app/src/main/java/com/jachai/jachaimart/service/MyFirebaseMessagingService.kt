package bd.com.evaly.ehealth.service

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bd.com.evaly.ehealth.models.notification.NotificationResponse
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.jachai.jachaimart.utils.constant.CommonConstants
import com.jachai.jachaimart.utils.sendNotification

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var broadcaster: LocalBroadcastManager? = null

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
                notificationResponse.title = remoteMessage.data[CommonConstants.NOTIFICATION_TITLE]
                notificationResponse.body = remoteMessage.data[CommonConstants.NOTIFICATION_BODY]
                notificationResponse.type = remoteMessage.data[CommonConstants.NOTIFICATION_TYPE]
                notificationResponse.resourceId =
                    remoteMessage.data[CommonConstants.NOTIFICATION_RESOURCE_ID]
                notificationResponse.imageUrl =
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
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("Jachai_FCM", "sendRegistrationTokenToServer($token)")
    }


}