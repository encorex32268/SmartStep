@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.lihan.smartstep.dashboard.presentation

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.bottomsheet.SmartStepModalBottomSheet
import com.lihan.smartstep.dashboard.presentation.components.AppDrawer
import com.lihan.smartstep.core.presentation.design_system.topbar.SmartStepTopbar
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.util.ObserveAsEvents
import com.lihan.smartstep.dashboard.presentation.components.DatePickerDialog
import com.lihan.smartstep.dashboard.presentation.components.EditStepsDialog
import com.lihan.smartstep.dashboard.presentation.components.ExitInformationDialog
import com.lihan.smartstep.dashboard.presentation.components.ResetTodayStepDialog
import com.lihan.smartstep.dashboard.presentation.components.StepCounterCard
import com.lihan.smartstep.dashboard.presentation.components.StepGoalBottomSheet
import com.lihan.smartstep.dashboard.presentation.permission.ActivityRecognitionPermissionEffect
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardRoot(
    onExitApp: () -> Unit,
    onNavigateToProfileSettings: () -> Unit,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when(event){
            DashboardEvent.NavigateToProfileSettings -> onNavigateToProfileSettings()
        }
    }

    DashboardScreen(
        state = state,
        onAction = { action ->
            when(action){
                DashboardAction.OnExitOKClick -> onExitApp()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ActivityRecognitionPermissionEffect(
            isShowBackgroundAccessBottomSheet = state.isShowBackgroundAccessBottomSheet,
            isShowAllowAccessBottomSheet = state.isShowAllowAccessBottomSheet,
            isShowEnableAccessManuallyBottomSheet = state.isShowEnableAccessManuallyBottomSheet,
            onNotGranted = {
                onAction(DashboardAction.OnShowAllowAccessBottomSheet)
            },
            onShouldShowRationale = {
                onAction(DashboardAction.OnShowAllowAccessBottomSheet)
            },
            onPermanentlyDenied = {
                onAction(DashboardAction.OnShowEnableAccessManuallyBottomSheet)
            },
            onShowBackgroundAccess = {
                onAction(DashboardAction.OnShowBackgroundAccessBottomSheet)
            },
            onBackgroundAccessContinueClick = {
                onAction(DashboardAction.OnBackgroundAccessContinueClick)
            }
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(
        modifier = modifier.fillMaxSize(),
        items = state.drawerItems,
        onItemClick = { drawerType ->
            if(drawerState.isOpen){
                scope.launch {
                    drawerState.close()
                }
            }
           onAction(DashboardAction.OnDrawerItemClick(drawerType))
        },
        drawerState = drawerState,
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SmartStepTopbar(
                        title = stringResource(R.string.dashboard_title),
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (drawerState.isClosed){
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = AppIcons.Menu,
                                    contentDescription = null,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    StepCounterCard(
                        currentSteps = 5000
                    )
                }

            }
            if (state.isShowExitDialog){
                ExitInformationDialog(
                    onDismissRequest = {
                        onAction(DashboardAction.OnDismissExitDialog)
                    },
                    onOkClick = {
                        onAction(DashboardAction.OnExitOKClick)
                    }
                )
            }
            if (state.isShowStepGoalBottomSheet){
                SmartStepModalBottomSheet(
                    dragHandle = null,
                    onDismissRequest = {
                        onAction(DashboardAction.OnDismissStepGoalBottomSheet)
                    },
                    properties = ModalBottomSheetProperties(),
                    content = {
                        StepGoalBottomSheet(
                            value = state.stepGoalPickerData.value,
                            items = state.stepGoalPickerData.items,
                            onSave = { step ->
                                onAction(DashboardAction.OnStepGoalBottomSheetSaveClick(step))
                            },
                            onCancel = {
                                onAction(DashboardAction.OnDismissStepGoalBottomSheet)
                            }
                        )
                    }
                )
            }
            if (state.isShowEditStepsDialog){
                EditStepsDialog(
                    dateTime = state.dateTime,
                    stepsTextFieldState = state.editStepsTextFieldState,
                    onSave = {
                        onAction(DashboardAction.OnEditStepsSaveClick)
                    },
                    onDone = {
                        //remove focus & hide keyboard
                    },
                    onCancel = {
                        onAction(DashboardAction.OnEditStepsCancelClick)
                    },
                    onDateFieldClick = {
                        onAction(DashboardAction.OnEditStepsFieldClick)
                    }
                )
                if (state.isShowDatePickerDialog){
                    DatePickerDialog(
                        initialEpochMillis = state.dateTime,
                        onSave = { newTimeLong ->
                            onAction(DashboardAction.OnDatePickerSaveClick(newTimeLong))
                        },
                        onCancel = {
                            onAction(DashboardAction.OnDatePickerCancelClick)
                        }
                    )
                }
            }
            if (state.isShowResetDialog){
                ResetTodayStepDialog(
                    onCancel = {
                        onAction(DashboardAction.OnResetTodayCancelClick)
                    },
                    onReset = {
                        onAction(DashboardAction.OnResetTodayResetClick)
                    }
                )
            }
        }
    )



}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    SmartStepTheme {
        DashboardScreen(
            state = DashboardState(
                isShowExitDialog = true
            ),
            onAction = {}
        )
    }
}
