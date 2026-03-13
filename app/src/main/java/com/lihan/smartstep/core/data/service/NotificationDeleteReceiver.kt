package com.lihan.smartstep.core.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class NotificationDeleteReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("NotificationDeleteReceiver Receiver Stop broadcast")
        val stopIntent = Intent(context, CountingStepService::class.java).apply {
            action = CountingStepService.DELETE
        }
        context?.startService(stopIntent)
    }
}