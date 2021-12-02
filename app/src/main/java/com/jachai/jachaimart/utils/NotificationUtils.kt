package com.jachai.jachaimart.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import bd.com.evaly.ehealth.models.notification.NotificationResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.google.gson.Gson
import com.jachai.jachaimart.MainActivity
import com.jachai.jachaimart.R
import com.jachai.jachaimart.utils.constant.CommonConstants
import java.util.concurrent.ExecutionException

private const val NOTIFICATION_ID = 8888
const val NOTIFICATION_CHANNEL_ID = CommonConstants.NOTIFICATION_CHANNEL

fun sendNotification(
    notificationResponse: NotificationResponse,
    context: Context
) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = getNotificationChannel(notificationResponse)
        notificationManager?.createNotificationChannel(channel)
    }
    val soundUri = getSoundUri()
    val pendingIntent = getPendingIntent(context, notificationResponse)

    // TODO ADD SMALL ICON
    val notificationBuilder =
        getNotificationBuilder(context, notificationResponse, soundUri, pendingIntent)
    notificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNotificationChannel(
    response: NotificationResponse
): NotificationChannel {
    val notificationChannel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        "My Notifications",
        NotificationManager.IMPORTANCE_HIGH
    )
    // Configure the notification channel.
    notificationChannel.apply {
        description = response.body
        enableLights(true)
        lightColor = Color.RED
        vibrationPattern = longArrayOf(0, 500, 250)
        enableVibration(true)
    }
    return notificationChannel
}

fun getSoundUri(): Uri {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
}

fun getPendingIntent(context: Context, response: NotificationResponse): PendingIntent {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra(CommonConstants.NOTIFICATION_DATA, Gson().toJson(response))
    return PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_ONE_SHOT
    )
}

fun getNotificationBuilder(
    context: Context,
    response: NotificationResponse,
    soundUri: Uri,
    pendingIntent: PendingIntent,
): NotificationCompat.Builder {
    val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
    builder
        .setContentTitle(response.title)
        .setContentText(response.body)
        .setAutoCancel(true)
        .setSound(soundUri)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentIntent(pendingIntent).priority = Notification.PRIORITY_MAX

    if (response.imageUrl != null) {
        try {
            val bitmapFutureTarget: FutureTarget<Bitmap> = Glide.with(context)
                .asBitmap()
                .load(response.imageUrl)
                .submit()
            val imageBitmap = bitmapFutureTarget.get()
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
                .bigPicture(imageBitmap)
                .setBigContentTitle(response.title)
                .setSummaryText(response.body)
            builder.setStyle(bigPictureStyle)
            Glide.with(context).clear(bitmapFutureTarget)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    } else {
        builder.setStyle(
            NotificationCompat.BigTextStyle().bigText(response.body)
        )
    }
    return builder
}