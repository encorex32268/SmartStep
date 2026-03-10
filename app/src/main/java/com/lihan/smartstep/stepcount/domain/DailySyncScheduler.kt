package com.lihan.smartstep.stepcount.domain

import com.lihan.smartstep.stepcount.domain.model.DailyStep

interface DailySyncScheduler {
    fun triggerSync(dailyStep: DailyStep)
}