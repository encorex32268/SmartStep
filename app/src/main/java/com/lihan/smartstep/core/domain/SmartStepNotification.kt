package com.lihan.smartstep.core.domain

import android.app.Notification

interface SmartStepNotification {
    fun sendNotification()
    fun getNotification(): Notification
}