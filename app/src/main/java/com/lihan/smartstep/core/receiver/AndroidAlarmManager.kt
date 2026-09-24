package com.lihan.smartstep.core.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

class AndroidAlarmManager(
    private val context: Context
): AppAlarmManager{

    companion object {
        private const val TAG = "AndroidAlarmManager"
        private const val ALARM_REQUEST_CODE = 1001
    }

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun setupBackupAlarm(): Boolean {
        if (!canScheduleExactAlarms()) {
            Log.w(TAG, "Cannot schedule exact alarm: permission not granted.")
            return false
        }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // 如果今天 00:01 已經過了，就設為明天 00:01
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        // 2. 建立 PendingIntent 指向 BroadcastReceiver
        val intent = Intent(context, ExactAlarmReceiver::class.java).apply {
            action = ExactAlarmReceiver.ACTION_EXACT_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 3. 設定精確鬧鐘（Doze 模式也能喚醒）
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d(TAG, "Exact backup alarm scheduled for ${calendar.time}")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Exception when scheduling exact alarm", e)
            return false
        }

    }

    override fun cancelBackupAlarm() {
        val intent = Intent(context, ExactAlarmReceiver::class.java).apply {
            action = ExactAlarmReceiver.ACTION_EXACT_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
            Log.d(TAG, "Exact backup alarm cancelled.")
        }
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            alarmManager.canScheduleExactAlarms()
        }else{
            true
        }
    }
}