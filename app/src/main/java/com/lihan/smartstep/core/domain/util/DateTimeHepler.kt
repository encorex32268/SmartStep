package com.lihan.smartstep.core.domain.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateTimeHelper {

    fun startEndTimestampRange(): LongRange {
        val now = LocalDateTime.now()
        val isSunday = now.dayOfWeek == DayOfWeek.SUNDAY
        val timestamp  = if (isSunday){
            val startTimestamp = now.getDayStartTimestamp()
            val endTime = now.with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
            val endTimestamp = endTime.getDayStartTimestamp()

            Pair(startTimestamp,endTimestamp)
        }else{
            val startTime = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            val startTimestamp = startTime.getDayStartTimestamp()
            val endTime = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
            val endTimestamp = endTime.getDayStartTimestamp()

            Pair(startTimestamp,endTimestamp)
        }
        return LongRange(start = timestamp.first , endInclusive = timestamp.second)
    }

    private fun LocalDateTime.getDayStartTimestamp(): Long {
        return LocalDateTime.of(
            this.year,
            this.month,
            this.dayOfMonth,
            0,0
        ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    //Display: Nov 16 ~ Nov 22
    fun getWeekDisplay(plusWeek: Long): String {
        val today = LocalDateTime.now()
            .plusWeeks(plusWeek)
            .atZone(ZoneId.systemDefault())
        val mondayDateTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sundayDateTime = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val startDisplayName = mondayDateTime.toShortDisplay()
        val endDisplayName = sundayDateTime.toShortDisplay()

        return "$startDisplayName ~ $endDisplayName"
    }

    private fun ZonedDateTime.toShortDisplay(): String {
        return "${this.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${this.dayOfMonth}"
    }


    fun getWeek(plusWeek: Long): LongRange {
        val now = LocalDateTime.now()
        val today = LocalDateTime
            .of(
                now.year,
                now.month,
                now.dayOfMonth,
                0,0
            )
            .plusWeeks(plusWeek)
            .atZone(ZoneId.systemDefault())
        val mondayDateTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sundayDateTime = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return LongRange(start = mondayDateTime.toInstant().toEpochMilli() , endInclusive = sundayDateTime.toInstant().toEpochMilli())
    }


    fun Long.getDayOfWeek(): String{
        return Instant
            .ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

}