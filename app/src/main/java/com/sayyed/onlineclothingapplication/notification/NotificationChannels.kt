package com.sayyed.onlineclothingapplication.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannels (val context: Context) {
    val CHANNEL_HIGH: String = "ChannelHigh"
    val CHANNEL_LOW: String = "ChannelLow"

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_HIGH,
                "Channel High",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "High priority"

            val channel2 = NotificationChannel(
                CHANNEL_LOW,
                "Channel Low",
                NotificationManager.IMPORTANCE_LOW
            )
            channel2.description = "Low priority"

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}