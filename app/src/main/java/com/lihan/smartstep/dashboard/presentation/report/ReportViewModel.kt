@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.dashboard.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.util.DateTimeHelper
import com.lihan.smartstep.dashboard.presentation.report.mapper.toDailyInfoUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ReportViewModel(
    private val dailyStepsRepository: DailyStepsRepository
): ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ReportState())
    val state = _state.onStart {
        if (!hasLoadedInitialData) {
            observeWeekSelector()
            hasLoadedInitialData = true
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ReportState()
        )

    fun onAction(action: ReportAction) {
        when (action) {
            else -> Unit
        }
    }


    private fun observeWeekSelector(){

        state
            .map { it.week }
            .flatMapLatest {  week ->
                val weekTimestamp = DateTimeHelper.getWeek(week.toLong())
                println("startTime: ${weekTimestamp.first}")
                dailyStepsRepository.getWeekDailyStepsList(
                    startTimestamp = weekTimestamp.first,
                    endTimestamp = weekTimestamp.last
                )
            }.onEach { dailySteps ->
                val dailyInfos = dailySteps.map { it.toDailyInfoUI() }
                println("Data: ${dailySteps.size}")
                println("Info Data: $dailyInfos")

                _state.update { it.copy(
                    dailyInfo = dailyInfos
                ) }
            }
            .launchIn(viewModelScope)



    }
}