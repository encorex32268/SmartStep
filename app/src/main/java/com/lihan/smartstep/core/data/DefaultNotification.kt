package com.lihan.smartstep.core.data

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.lihan.smartstep.R
import com.lihan.smartstep.SmartStepApplication.Companion.CHANNEL_ID
import com.lihan.smartstep.core.domain.SmartStepNotification

class DefaultNotification(
    private val context: Context
): SmartStepNotification {


    private val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    override fun sendNotification() {
        notificationManager.notify(101,getNotification())

    }

    override fun getNotification(): Notification {

        val notificationLayout = RemoteViews(context.packageName , R.layout.notification_small)
        val notificationLayoutExpand = RemoteViews(context.packageName , R.layout.notification_large)

        return NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpand)
            .build()
    }
}