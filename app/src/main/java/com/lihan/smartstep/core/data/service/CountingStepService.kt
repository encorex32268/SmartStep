package com.lihan.smartstep.core.data.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.lihan.smartstep.R
import com.lihan.smartstep.SmartStepApplication.Companion.CHANNEL_ID
import com.lihan.smartstep.core.data.AppUserInfo
import com.lihan.smartstep.core.data.SmartStepTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class CountingStepService: Service(){

    private val smartStepTracker by inject<SmartStepTracker>()
    private val userInfoDataStore by inject<AppUserInfo>()

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private var serviceJob: Job? = null
    private var serviceSaveDataJob: Job? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            START -> start()
            STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)

    }


    private fun stop() {
        //save data when service stop
        val stepDate = smartStepTracker.stepDate.value

        smartStepTracker.stopTracking()

        serviceSaveDataJob?.cancel()
        serviceSaveDataJob = CoroutineScope(NonCancellable).launch {
            userInfoDataStore.apply {
                updateTodayTimer(stepDate.countingTimestamp)
                updateTodaySteps(stepDate.steps)
            }
        }
        stopSelf()
    }

    @SuppressLint("ForegroundServiceType")
    private fun start(){

        val notificationLayout = RemoteViews(this.packageName, R.layout.notification_small)
        val notificationLayoutExpand = RemoteViews(this.packageName , R.layout.notification_large)

        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.notification_icon)

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setBadgeIconType(R.drawable.notification_icon)
            .setSmallIcon(R.drawable.notification_icon)
            .setLargeIcon(largeIconBitmap)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpand)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setOngoing(false)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setDeleteIntent(createDeleteIntent())


        serviceJob?.cancel()
        serviceJob = CoroutineScope(Dispatchers.Default).launch {
            smartStepTracker.stepDate.collectLatest { data ->
                val steps = data.steps.toString()
                val calories = data.calories.toString()

                val progressInt = (steps.toFloat() / data.goalSteps * 100).toInt()

                notificationLayout.setTextViewText(R.id.notification_steps, steps)
                notificationLayout.setTextViewText(R.id.notification_cal, calories)
                notificationLayout.setInt(R.id.steps_progress , "setProgress" , progressInt)

                notificationLayoutExpand.setTextViewText(R.id.notification_steps, steps)
                notificationLayoutExpand.setTextViewText(R.id.notification_cal, calories)
                notificationLayoutExpand.setInt(R.id.steps_progress , "setProgress" , progressInt)

                notificationManager.notify(1, notification.build())
            }
        }

        startForeground(1,notification.build())
    }

    private fun createDeleteIntent(): PendingIntent {
        val deleteIntent = Intent(this, NotificationDeleteReceiver::class.java)
        val deletePendingIntent = PendingIntent.getBroadcast(
            this, 101, deleteIntent, PendingIntent.FLAG_IMMUTABLE
        )
        return deletePendingIntent
    }

    companion object{
        const val START = "start_counting"
        const val STOP = "stop_counting"
    }


}