package com.lihan.smartstep.stepcount.presentation.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.ui.text.intl.Locale
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date

object DateTimeUtils {

    val yearRange: IntRange
        get() = IntRange(start = 1920 , endInclusive = 2100)

    val monthRange: IntRange
        get() = IntRange(start = 1, endInclusive = 12)

    fun getDayRange(year: Int,month: Int): IntRange{
        val maxDay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            YearMonth.of(year, month+1).lengthOfMonth()
        } else {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month + 1)
            }.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        return 1..maxDay
    }

    fun getThisYear(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().year.toString()
        } else {
            (Date().year + 1900).toString()
        }
    }

    @SuppressLint("DefaultLocale")
    fun getThisMonth(): String {
        val month = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().monthValue
        } else {
            Date().month + 1
        }
        return month.toString()
    }

    @SuppressLint("DefaultLocale")
    fun getToday(): String {
        val day = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().dayOfMonth
        } else {
            Date().day + 1
        }
        return day.toString()
    }

    fun getTodayDate(): String {
        val now = LocalDateTime.now()
        return "${now.year}/${now.monthValue}/${now.dayOfMonth}"

    }

    fun getTodayDayOfWeekShort(): String{
        val now = LocalDateTime.now()
        val dayOfWeek = now.dayOfWeek
        return DayOfWeek.of(dayOfWeek.value).getDisplayName(TextStyle.SHORT, java.util.Locale.ENGLISH)
    }

    /**
     *  Get Today
     *  ex: 2026/03/13 0:0:0
     */
    fun getTodayEpochMilli(): Long {
        val now = LocalDateTime.now()
        return LocalDateTime
            .of(now.year,now.monthValue,now.dayOfMonth, 0 , 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }



    fun getDaysOfWeek(firstDay: DayOfWeek = DayOfWeek.SUNDAY): List<DayOfWeek> {
        val days = DayOfWeek.entries
        val firstIndex = days.indexOf(firstDay)
        return days.subList(firstIndex, days.size) + days.subList(0, firstIndex)
    }

}