@file:OptIn(ExperimentalTime::class)

package com.lihan.smartstep.stepcount.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

object Timer {

    fun emitDuration(): Flow<Duration> = flow{
        var lastTime = Clock.System.now().toEpochMilliseconds()
        while(true){
            delay(200L)
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val elapsedTime = currentTime - lastTime
            emit(elapsedTime.milliseconds)
            lastTime = currentTime
        }
    }
}