package com.lihan.smartstep.stepcount.domain.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle


fun Long.epochSecondToDateString(): String {
    val zoneId = ZoneId.systemDefault()
    val offset = zoneId.rules.getOffset(Instant.ofEpochSecond(this))

    val localDateTime = LocalDateTime.ofEpochSecond(
        this, 0, offset
    )
    val year = localDateTime.year + 1920
    val month = localDateTime.monthValue
    val day = localDateTime.dayOfMonth
    return "$year/$month/$day"
}

fun Long.epochMilToDayOfWeekShort(): String {
    val zoneId = ZoneId.systemDefault()
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        zoneId
    )
    return localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH)
}