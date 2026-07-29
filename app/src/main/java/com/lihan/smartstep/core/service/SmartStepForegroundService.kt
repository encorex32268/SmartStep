package com.lihan.smartstep.core.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.lihan.smartstep.R
import com.lihan.smartstep.core.data.hasActivityRecognitionPermission
import java.text.NumberFormat
import kotlin.math.roundToInt

class SmartStepForegroundService: Service() {



    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "foreground_service_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        const val STEPS_KEY = "steps"
        const val CALORIES_KEY = "calories"
        const val PROGRESS_KEY = "progress"
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> {
                val steps = intent.getIntExtra(STEPS_KEY,0)
                val calories = intent.getIntExtra(CALORIES_KEY,0)
                val progress = intent.getIntExtra(PROGRESS_KEY,0)
                startSmartStepForegroundService(steps,calories,progress)
            }
            ACTION_STOP -> stopSmartStepForegroundService()
        }
        return START_STICKY
    }

    private fun stopSmartStepForegroundService(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startSmartStepForegroundService(
        steps: Int,
        calories: Int,
        progress: Int
    ){
        startForeground(
            NOTIFICATION_ID,
            createNotification(
                context = applicationContext,
                steps = steps,
                calories = calories,
                progress = progress
            )
        )
    }

    private fun createNotification(
        context: Context,
        steps: Int,
        calories: Int,
        progress: Int
    ): Notification {

        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed).apply {
            setTextViewText(R.id.tv_steps, NumberFormat.getNumberInstance().format(steps))
            setTextViewText(R.id.tv_calories, calories.toString())
            setProgressBar(R.id.progress_bar, 100, progress, false)
        }
        val expandedView = RemoteViews(packageName, R.layout.notification_expanded).apply {
            setTextViewText(R.id.tv_steps_exp, NumberFormat.getNumberInstance().format(steps))
            setTextViewText(R.id.tv_calories_exp, calories.toString())
            setProgressBar(R.id.progress_bar_exp, 100, progress, false)

            val actionIntent = Intent(context, SmartStepForegroundService::class.java).apply {
                action = "ACTION_CLICK_DETAIL"
            }
            val pendingIntent = PendingIntent.getService(
                context,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            setOnClickPendingIntent(R.id.btn_action_exp, pendingIntent)
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.step)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setOngoing(true)
            .build()
    }


}