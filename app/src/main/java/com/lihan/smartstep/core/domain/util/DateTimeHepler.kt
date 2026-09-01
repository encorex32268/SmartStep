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


    //previous , this next , weeks
    fun getWeeks(weeks: Long): List<LongRange> {
        val now = LocalDateTime.now()
        val today = LocalDateTime
            .of(
                now.year,
                now.monthValue,
                now.dayOfMonth,
                0,0
            )
            .atZone(ZoneId.systemDefault()).plusWeeks(weeks)
        val thisWeekStartTime = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val thisWeekEndTime = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val previousWeekStartTime = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val previousWeekEndTime = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val nextWeekStartTime = today.plusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val nextWeekEndTime = today.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        return listOf(
            LongRange(start = previousWeekStartTime.toInstant().toEpochMilli(), endInclusive = previousWeekEndTime.toInstant().toEpochMilli()),
            LongRange(start = thisWeekStartTime.toInstant().toEpochMilli() , endInclusive = thisWeekEndTime.toInstant().toEpochMilli()),
            LongRange(start = nextWeekStartTime.toInstant().toEpochMilli() , endInclusive = nextWeekEndTime.toInstant().toEpochMilli()),
        )
    }


    fun Long.getDayOfWeek(): String{
        return Instant
            .ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

}