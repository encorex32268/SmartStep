package com.lihan.smartstep.core.presentation.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.Instant

private val defaultDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

fun Long.toFormattedTime(
    pattern: String? = null,
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = if (pattern != null) DateTimeFormatter.ofPattern(pattern) else defaultDateFormatter
    return java.time.Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .format(formatter)
}