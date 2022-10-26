package com.fourfifths.android.baedalsharing.data.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fourfifths.android.baedalsharing.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {
    private val TAG = FirebaseService::class.java.simpleName
    private val CHANNEL_ID = "matching_result_channel"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, message.notification?.title.toString())
        Log.d(TAG, message.notification?.body.toString())

        val title = message.notification?.title.toString()
        val body = message.notification?.body.toString()

        createNotification()
        sendNotification(title, body)
    }

    private fun createNotification() {
        val name = "매칭 결과"
        val descriptionText = "매칭 성공 결과 및 실패 여부를 안내 받습니다."
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = descriptionText

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(title: String, body: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }
    }
}