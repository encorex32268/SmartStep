package com.lihan.smartstep.stepcount.domain.worker

interface DailySyncScheduler {
    suspend fun triggerSync()
}