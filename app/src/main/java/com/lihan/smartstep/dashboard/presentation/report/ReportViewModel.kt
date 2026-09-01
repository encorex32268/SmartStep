@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lihan.smartstep.dashboard.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lihan.smartstep.core.domain.DailyStepsRepository
import com.lihan.smartstep.core.domain.util.DateTimeHelper
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType
import com.lihan.smartstep.dashboard.presentation.report.mapper.toDailyInfoUI
import com.lihan.smartstep.dashboard.presentation.report.model.DailyInfoUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Collections.rotate
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
            ReportAction.OnBackClick -> Unit
            is ReportAction.OnDailyInfoItemClick -> dailyInfoItemClick(action.dailyInfoUI)
            is ReportAction.OnReportTypeClick -> {
                _state.update { it.copy(
                    reportType = action.type
                ) }
            }
            ReportAction.OnNextWeekClick -> {
                _state.update { it.copy(
                    week = it.week + 1
                ) }
            }
            ReportAction.OnPreviousClick -> {
                _state.update { it.copy(
                    week = it.week - 1
                ) }
            }
        }
    }

    private fun dailyInfoItemClick(dailyInfoUI: DailyInfoUI){

        _state.update { it.copy(
            currentValue = when(it.reportType){
                ReportType.Steps -> dailyInfoUI.steps
                ReportType.Calories -> dailyInfoUI.calories
                ReportType.Minutes -> dailyInfoUI.spentTime
                ReportType.Kilometers -> dailyInfoUI.distance
            }
        ) }


    }


    private fun observeWeekSelector(){

        state
            .map { it.week }
            .flatMapLatest {  week ->
                val weekTimestamp = DateTimeHelper.getWeeks(week.toLong())
                dailyStepsRepository.getWeekDailyStepsList(
                    startTimestamp = weekTimestamp.first().first,
                    endTimestamp = weekTimestamp.last().last
                ).map {
                    weekTimestamp to it
                }
            }.onEach { (weeks , dailySteps)  ->
                val previous = weeks[0]
                val thisWeek = weeks[1]
                val nextWeek = weeks[2]
                val groupBy = dailySteps.groupBy { item ->
                    weeks.firstOrNull { range -> item.createAt in range }
                }

                val dailyInfo = groupBy[thisWeek]?.map { dailyStep -> dailyStep.toDailyInfoUI() }?:emptyList()

                val modifierDailyInfo = dailyInfo.toMutableList()

                DayOfWeek.entries.forEachIndexed { index, dayOfWeek ->
                    if (modifierDailyInfo.getOrNull(index) == null){
                        val dayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        modifierDailyInfo.add(
                            index = index,
                            element = DailyInfoUI(
                                dayOfWeek = dayName,
                                status = DailyInfoStatus.NotYet,
                                spentTime = "0",
                                stepGoal = "0",
                                calories = "0",
                                distance = "0.0",
                                steps = "0"
                            )
                        )
                    }
                }

                _state.update { it.copy(
                    dailyInfo = modifierDailyInfo,
                    isNextWeekEnabled = groupBy[nextWeek]?.isNotEmpty()?: false,
                    isPreviousWeekEnabled = groupBy[previous]?.isNotEmpty()?: false
                ) }
            }
            .launchIn(viewModelScope)



    }
}