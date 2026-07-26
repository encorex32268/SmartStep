package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DatePickerState(
    initialEpochMillis: Long,
    val startYear: Int = 1950,
    val endYear: Int = LocalDate.now().year
) {
    private val initialDate = Instant.ofEpochMilli(initialEpochMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    var selectedYear by mutableIntStateOf(initialDate.year)
        private set

    var selectedMonth by mutableIntStateOf(initialDate.monthValue)
        private set

    var selectedDay by mutableIntStateOf(initialDate.dayOfMonth)
        private set

    val yearList: List<String> = (startYear..endYear).map { it.toString() }

    val monthList: List<String> = (1..12).map { it.toString() }

    val dayList: List<String>
        get() {
            val lengthOfMonth = LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth()
            return (1..lengthOfMonth).map { it.toString() }
        }

    fun onYearSelected(yearStr: String) {
        selectedYear = yearStr.toIntOrNull() ?: selectedYear
        coerceDayIfNeeded()
    }

    fun onMonthSelected(monthStr: String) {
        selectedMonth = monthStr.toIntOrNull() ?: selectedMonth
        coerceDayIfNeeded()
    }

    fun onDaySelected(dayStr: String) {
        selectedDay = dayStr.toIntOrNull() ?: selectedDay
    }

    private fun coerceDayIfNeeded() {
        val maxDays = LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth()
        if (selectedDay > maxDays) {
            selectedDay = maxDays
        }
    }

    fun getSelectedEpochMillis(): Long {
        return LocalDate.of(selectedYear, selectedMonth, selectedDay)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}

@Composable
fun rememberDatePickerState(
    initialEpochMillis: Long
): DatePickerState {
    return remember(initialEpochMillis) {
        DatePickerState(initialEpochMillis)
    }
}