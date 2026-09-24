package com.lihan.smartstep.dashboard.presentation.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.topbar.SmartStepTopbar
import com.lihan.smartstep.core.presentation.ui.theme.AICoachBackIcon
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.util.toNumberString
import com.lihan.smartstep.dashboard.presentation.aicoach.AICoachAction
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoCard
import com.lihan.smartstep.dashboard.presentation.report.components.DailyInfoStatus
import com.lihan.smartstep.dashboard.presentation.report.components.InfoCard
import com.lihan.smartstep.dashboard.presentation.report.components.ReportBottomBar
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.toBottomItemName
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.toDailyInfoCardUnit
import com.lihan.smartstep.dashboard.presentation.report.components.WeekSelector
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportRoot(
    onBack: () -> Unit,
    viewModel: ReportViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReportScreen(
        state = state,
        onAction = { action ->
            when(action){
                ReportAction.OnBackClick -> onBack()
                else -> Unit
            }
        }
    )
}

@Composable
private fun ReportScreen(
    state: ReportState,
    onAction: (ReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SmartStepTopbar(
                title = stringResource(R.string.report),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(ReportAction.OnBackClick)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = AppIcons.ArrowLeft,
                            contentDescription = null,
                            tint = AICoachBackIcon
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard(
                    type = state.reportType,
                    average = state.average,
                    value = state.currentValue
                )
                WeekSelector(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onNext = { onAction(ReportAction.OnNextWeekClick) },
                    onPrevious = { onAction(ReportAction.OnPreviousClick) },
                    currentWeek = state.currentWeek,
                    canNext = state.isNextWeekEnabled,
                    canPrevious = state.isPreviousWeekEnabled
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.dailyInfo){ dailyInfoUI ->
                        val reportType = state.reportType
                        DailyInfoCard(
                            dayOfWeek = dailyInfoUI.dayOfWeek,
                            status = dailyInfoUI.status,
                            type = reportType.toDailyInfoCardUnit(),
                            onItemClick = {
                                onAction(ReportAction.OnDailyInfoItemClick(dailyInfoUI))
                            },
                            value = when(reportType){
                                ReportType.Steps -> dailyInfoUI.steps
                                ReportType.Calories -> dailyInfoUI.calories
                                ReportType.Minutes -> dailyInfoUI.spentTime
                                ReportType.Kilometers -> dailyInfoUI.distance
                            },
                            description = if (reportType == ReportType.Steps && ((dailyInfoUI.stepGoal.filter { it.isDigit() }
                                    .toIntOrNull() ?: 0) > 0)) {
                                stringResource(R.string.daily_goal,dailyInfoUI.stepGoal)
                            }else ""
                        )
                    }
                    item {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ){
                ReportBottomBar(
                    currentType = state.reportType,
                    onItemClick = { type ->
                        onAction(ReportAction.OnReportTypeClick(type))
                    }
                )
            }
        }


    }



}

@Preview(showBackground = true)
@Composable
private fun ReportScreenPreview() {
    SmartStepTheme {
        ReportScreen(
            state = ReportState(),
            onAction = {}
        )

    }
}