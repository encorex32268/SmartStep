package com.lihan.smartstep.core.receiver

interface AppAlarmManager {
    fun setupBackupAlarm(): Boolean
    fun cancelBackupAlarm()
}