@file:OptIn(FlowPreview::class)

package com.lihan.smartstep.core.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.lihan.smartstep.R
import com.lihan.smartstep.app.MainActivity
import com.lihan.smartstep.core.data.hasActivityRecognitionPermission
import com.lihan.smartstep.core.domain.AppSensorManager
import com.lihan.smartstep.core.domain.model.StepMetrics
import com.lihan.smartstep.core.domain.usecase.GetStepMetricsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import java.text.NumberFormat
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

class SmartStepForegroundService: Service() {

    private val getStepMetricsUseCase by inject<GetStepMetricsUseCase>()
    private val coroutineScope by inject<CoroutineScope>()

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "foreground_service_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
    override fun onCreate() {
        super.onCreate()
        observeStepMetrics()
    }

    private fun observeStepMetrics() {
        getStepMetricsUseCase()
            .debounce(300.milliseconds)
            .map {
                createNotification(stepMetrics = it)
            }.onEach { notification ->
                updateNotification(notification)
            }
            .launchIn(coroutineScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START ->startSmartStepForegroundService()
            ACTION_STOP -> stopSmartStepForegroundService()
        }
        return START_STICKY
    }

    private fun stopSmartStepForegroundService(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startSmartStepForegroundService(){
        updateNotification(createNotification(null))
    }

    private fun updateNotification(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )
        }else {
            startForeground(
                NOTIFICATION_ID,
                notification
            )
        }
    }

    private fun createNotification(
        stepMetrics: StepMetrics?
    ): Notification {
        val steps = NumberFormat.getNumberInstance().format(stepMetrics?.steps?:0)
        val kcal = stepMetrics?.kcal.toString()
        val progress = ((stepMetrics?.progress ?: (0f * 100))).roundToInt()

        val actionIntent = Intent(this@SmartStepForegroundService, MainActivity::class.java).apply {
            action = "ACTION_CLICK_DETAIL"
        }
        val pendingIntent = PendingIntent.getActivity(
            this@SmartStepForegroundService,
            0,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val collapsedView = RemoteViews(packageName, R.layout.notification_collapsed).apply {
            setTextViewText(R.id.tv_steps, steps)
            setTextViewText(R.id.tv_calories, kcal)
            setProgressBar(R.id.progress_bar, 100, progress, false)
        }
        val expandedView = RemoteViews(packageName, R.layout.notification_expanded).apply {
            setTextViewText(R.id.tv_steps_exp, steps)
            setTextViewText(R.id.tv_calories_exp, kcal)
            setProgressBar(R.id.progress_bar_exp, 100, progress, false)

            setOnClickPendingIntent(R.id.btn_action_exp, pendingIntent)
        }

        return NotificationCompat.Builder(this@SmartStepForegroundService, CHANNEL_ID)
            .setSmallIcon(R.drawable.step)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSmartStepForegroundService()
    }

}