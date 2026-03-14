@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.lihan.smartstep.stepcount.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.lihan.smartstep.R
import com.lihan.smartstep.core.data.isIgnoringBatteryOptimizations
import com.lihan.smartstep.stepcount.presentation.components.BackgroundAccessModal
import com.lihan.smartstep.stepcount.presentation.components.DailyStepsCard
import com.lihan.smartstep.stepcount.presentation.components.DatePickerDialog
import com.lihan.smartstep.stepcount.presentation.components.EditStepsDialog
import com.lihan.smartstep.stepcount.presentation.components.EnableAccessModal
import com.lihan.smartstep.stepcount.presentation.components.ExitModal
import com.lihan.smartstep.stepcount.presentation.components.MotionSensorsAccessModal
import com.lihan.smartstep.stepcount.presentation.components.StepResetDialog
import com.lihan.smartstep.stepcount.presentation.components.StepsCard
import com.lihan.smartstep.stepcount.presentation.components.StepsGoalModal
import com.lihan.smartstep.stepcount.presentation.drawer.closeDrawerActions
import com.lihan.smartstep.stepcount.presentation.drawer.drawerItems
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.utils.DateTimeUtils.getDaysOfWeek
import com.lihan.smartstep.stepcount.presentation.utils.isAppInForeground
import com.lihan.smartstep.ui.theme.BackgroundMain
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.bodyLargeMedium
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SmartStepScreenRoot(
    onExit: () -> Unit,
    onNavigateToPersonSettings: () -> Unit,
    viewModel: SmartStepViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val showPowerWarning = rememberPowerManagerStatus()



    SmartStepScreen(
        showPowerWarning = showPowerWarning,
        drawerState = drawerState,
        state = state,
        onAction = { action ->
            if (action in closeDrawerActions) {
                scope.launch { drawerState.close() }
            }
            when (action) {
                SmartStepAction.OnMenuClick -> {
                    scope.launch {
                        drawerState.open()
                    }
                }

                SmartStepAction.OnExitClick -> {
                    onExit()
                }

                SmartStepAction.OnPersonSettingsClick -> {
                    onNavigateToPersonSettings()
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}


@SuppressLint("BatteryLife")
@Composable
fun SmartStepScreen(
    showPowerWarning: Boolean,
    drawerState: DrawerState,
    state: SmartStepState,
    onAction: (SmartStepAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Manifest.permission.ACTIVITY_RECOGNITION
    } else {
        //VERSION.SDK_INT < Q
        "com.google.android.gms.permission.ACTIVITY_RECOGNITION"
    }

    var hasPermissionRequest by rememberSaveable {
        mutableStateOf(false)
    }
    val activityRecognitionPermissionState = rememberPermissionState(
        permission = permission,
        onPermissionResult = {
            hasPermissionRequest = true
        }
    )

    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { granted ->
                if (granted){
                    //TODO: Start Service
                }
            }
        )
    } else {
        null
    }

    val backgroundRunningAccept = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val isIgnoringBatteryOptimizations = context.isIgnoringBatteryOptimizations()
        if (isIgnoringBatteryOptimizations){
            notificationPermission?.launchPermissionRequest()
        }
    }


    val isInForeground by isAppInForeground()

    LaunchedEffect(isInForeground) {
        if (isInForeground) {
            onAction(SmartStepAction.OnStopService)
        } else {
            if (context.isIgnoringBatteryOptimizations()){
                onAction(SmartStepAction.OnStartService)
            }
        }
    }

    LifecycleResumeEffect(activityRecognitionPermissionState.status) {
        when (val status = activityRecognitionPermissionState.status) {
            is PermissionStatus.Denied -> {
                if (hasPermissionRequest) {
                    val shouldShowRationale = status.shouldShowRationale
                    if (shouldShowRationale) {
                        onAction(SmartStepAction.OnShowSensorsAccessModal)
                    } else {
                        onAction(SmartStepAction.OnShowEnableAccessModal)
                    }
                } else {
                    activityRecognitionPermissionState.launchPermissionRequest()
                }

            }

            is PermissionStatus.Granted -> {
                onAction(SmartStepAction.OnPermissionGranted)
                if (showPowerWarning && !state.isShownBackgroundAccessModal) {
                    onAction(SmartStepAction.OnShowBackgroundAccessModalFirstTime)
                }
            }
        }
        onPauseOrDispose { }

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                drawerContainerColor = BackgroundSecondary,
                windowInsets = WindowInsets.statusBars
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    item {
                        if (showPowerWarning) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAction(SmartStepAction.OnShowBackgroundAccessModal)
                                    }
                                    .padding(vertical = 16.dp, horizontal = 24.dp),
                                text = stringResource(R.string.stop_counting_steps_issue),
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = TextPrimary
                            )
                            HorizontalDivider(color = StrokeMain)
                        }
                    }

                    itemsIndexed(drawerItems) { index, drawerItem ->
                        if (index != 0) {
                            HorizontalDivider(color = StrokeMain)
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (drawerItem.id) {
                                        R.string.step_goal -> onAction(SmartStepAction.OnStepGoalClick)
                                        R.string.personal_settings -> onAction(SmartStepAction.OnPersonSettingsClick)
                                        R.string.edit_steps_drawer -> onAction(SmartStepAction.OnEditStepsClick)
                                        R.string.reset_today_steps -> onAction(SmartStepAction.OnResetTodayStepsClick)
                                        R.string.exit -> onAction(SmartStepAction.OnExitClick)
                                    }
                                }
                                .padding(vertical = 16.dp, horizontal = 24.dp),
                            text = stringResource(drawerItem.id),
                            style = MaterialTheme.typography.bodyLargeMedium,
                            color = drawerItem.textColor
                        )

                    }
                }

            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundMain)
        ) {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundMain
                ),
                windowInsets = WindowInsets.systemBars,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SmartStepAction.OnMenuClick)
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.menu),
                            contentDescription = "Menu",
                            tint = TextPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.smart_step_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            )

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepsCard(
                modifier = Modifier
                    .widthIn(max = 394.dp)
                    .fillMaxWidth(),
                steps = state.step,
                stepsTotal = state.totalStep,
                distance = state.distance,
                calories = state.calories,
                timer = state.time,
                isCounting = state.isTrackingStep,
                onPauseClick = {
                    onAction(SmartStepAction.OnStopCounting)
                },
                onResumeClick = {
                    onAction(SmartStepAction.OnResumeCounting)
                },
                onEditClick = {
                    onAction(SmartStepAction.OnEditClick)
                }
            )
            Spacer(Modifier.height(8.dp))
            DailyStepsCard(
                average = state.average,
                dailySteps = state.dailySteps,
                modifier = Modifier
                    .widthIn(max = 394.dp)
                    .fillMaxWidth()
            )

        }

    }

    if (state.isShowStepGoal) {
        StepsGoalModal(
            value = state.totalStep.toString(),
            onDismiss = {
                onAction(SmartStepAction.OnDismissStepGoal)
            },
            onSave = {
                onAction(SmartStepAction.OnStepGoalSaveClick(it))
            },
            onCancel = {
                onAction(SmartStepAction.OnDismissStepGoal)
            }
        )
    }

    when {
        state.isShowSensorsModal -> {
            MotionSensorsAccessModal(
                onAllowAccessClick = {
                    activityRecognitionPermissionState.launchPermissionRequest()
                }
            )
        }

        state.isShowEnableAccessModal -> {
            EnableAccessModal(
                onOpenSettingsClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    context.startActivity(intent)
                }
            )
        }
    }

    if (state.isShowBackgroundAccessModal) {
        BackgroundAccessModal(
            onContinueClick = {
                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = "package:${context.packageName}".toUri()
                }
                backgroundRunningAccept.launch(intent)

                onAction(SmartStepAction.OnDismissBackgroundAccessModal)

            },
            onDismiss = {
                onAction(SmartStepAction.OnDismissBackgroundAccessModal)
            }
        )
    }

    if (state.isShowExitModal) {
        ExitModal(
            onOkClick = {
                onAction(SmartStepAction.OnExitOkClick)
            },
            onDismiss = {
                onAction(SmartStepAction.OnDismissExitModal)
            }
        )
    }

    if (state.isShowEditSteps) {
        EditStepsDialog(
            dateTextFieldState = state.editStepsDateTextFieldState,
            stepsTextState = state.editStepsStepsTextFieldState,
            onCancelClick = {
                onAction(SmartStepAction.OnDismissEditStepsDialog)
            },
            onSaveClick = {
                onAction(SmartStepAction.OnEditStepsSaveClick)
            },
            onDateClick = {
                onAction(SmartStepAction.OnShowDatePicker)
            }
        )
        if (state.isShowDatePicker) {
            DatePickerDialog(
                onSaveClick = {
                    onAction(SmartStepAction.OnDatePickerSaveClick(it))
                },
                onCancelClick = {
                    onAction(SmartStepAction.OnDismissDatePickerDialog)
                }
            )
        }
    }

    if (state.isShowResetStepsDialog) {
        StepResetDialog(
            onDismiss = {
                onAction(SmartStepAction.OnDismissResetStepsDialog)
            },
            onResetClick = {
                onAction(SmartStepAction.OnResetStepClick)
            }
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun SmartStepScreenPreview() {
    SmartStepTheme {
        val date = getDaysOfWeek().mapIndexed { index, week ->
            DailyStepUI(
                steps = (index*2000) .toString(),
                date = week.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                goalSteps = 6000.toString(),
                time = 0
            )
        }

        SmartStepScreen(
            state = SmartStepState(
                step = 1234,
                dailySteps = date,
                isShowStepGoal = false,
                isShowResetStepsDialog = false,
                isShowEditSteps = false,
                isShowDatePicker = false
            ),
            onAction = {},
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            showPowerWarning = false
        )
    }
}