package com.example.bike_computer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

fun createNotification(context: Context): Notification {
    val channelId = "location_channel"
    val channelName = "Location Tracking"

    val channel = NotificationChannel(
        channelId,
        channelName,
        NotificationManager.IMPORTANCE_LOW
    )

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)

    return NotificationCompat.Builder(context, channelId)
        .setContentTitle("Tracking Location")
        .setContentText("Location service is running")
        .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        .build()
}
