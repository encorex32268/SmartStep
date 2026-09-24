package com.lihan.smartstep.core.receiver
 
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lihan.smartstep.core.worker.SaveDailyStepsScheduler

class ExactAlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "ExactAlarmReceiver"
        const val ACTION_EXACT_ALARM = "com.lihan.smartstep.ACTION_EXACT_ALARM"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val action = intent?.action
        Log.d(TAG, "ExactAlarmReceiver received action: $action")

        when (action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                Log.d(TAG, "System event ($action) received. Rescheduling daily steps alarm/worker.")
                SaveDailyStepsScheduler.schedule(context)
            }
            else -> {
                Log.d(TAG, "Exact alarm triggered at scheduled time.")
                // 1. 在 Alarm 指定時間立即執行 Worker
                SaveDailyStepsScheduler.executeNow(context)

                // 2. 重新設定下一天的排程 (若設置成功等待下一次 Alarm，失敗則 fallback 繼續使用 periodic worker)
                SaveDailyStepsScheduler.schedule(context)
            }
        }
    }
}