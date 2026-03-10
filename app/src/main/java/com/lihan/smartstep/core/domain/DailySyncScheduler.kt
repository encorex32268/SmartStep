package com.lihan.smartstep.core.domain

interface DailySyncScheduler {
    suspend fun triggerSync()
}