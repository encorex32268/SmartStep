package com.lihan.smartstep.core.domain.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

object TimerFlow {
    fun timeAndEmit(interval: Duration = 400.milliseconds): Flow<Duration> {
        return flow {
            var previousTimeMark = TimeSource.Monotonic.markNow()
            while (true){
                delay(interval)
                val currentTimeMark = TimeSource.Monotonic.markNow()
                val elapsedTime = currentTimeMark - previousTimeMark
                emit(elapsedTime)
                previousTimeMark = currentTimeMark
            }
        }
    }
}