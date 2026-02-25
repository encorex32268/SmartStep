@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.lihan.smartstep.stepcount.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.runtime.SideEffect
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
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lihan.smartstep.R
import com.lihan.smartstep.stepcount.presentation.components.EnableAccessModal
import com.lihan.smartstep.stepcount.presentation.components.MotionSensorsAccessModal
import com.lihan.smartstep.stepcount.presentation.components.StepsCard
import com.lihan.smartstep.stepcount.presentation.components.StepsGoalModal
import com.lihan.smartstep.stepcount.presentation.drawer.closeDrawerActions
import com.lihan.smartstep.stepcount.presentation.drawer.drawerItems
import com.lihan.smartstep.ui.theme.BackgroundMain
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.bodyLargeMedium
import kotlinx.coroutines.launch

@Composable
fun SmartStepScreenRoot(
    onExit: () -> Unit,
    onNavigateToPersonSettings: () -> Unit,
    viewModel: SmartStepViewModel = viewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()



    SmartStepScreen(
        drawerState = drawerState,
        state = state,
        onAction = { action ->
            if (action in closeDrawerActions){
                scope.launch { drawerState.close() }
            }
            when(action){
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


@Composable
fun SmartStepScreen(
    drawerState: DrawerState,
    state: SmartStepState,
    onAction: (SmartStepAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Manifest.permission.ACTIVITY_RECOGNITION
    } else {
        //VERSION.SDK_INT < Q
        "com.google.android.gms.permission.ACTIVITY_RECOGNITION"
    }

    val activityRecognitionPermissionState = rememberPermissionState(
        permission = permission,
        onPermissionResult = { isGranted ->
            onAction(SmartStepAction.OnUpdatePermission(isGranted = isGranted))
            hasRequestedPermission = true
        }
    )

    LaunchedEffect(Unit) {
        activityRecognitionPermissionState.launchPermissionRequest()
    }

    LifecycleResumeEffect(activityRecognitionPermissionState.status) {
        when{
            activityRecognitionPermissionState.status.isGranted -> {
                onAction(SmartStepAction.OnResumeGetGranted)
            }
            activityRecognitionPermissionState.status.shouldShowRationale ->{
                onAction(SmartStepAction.OnShowSensorsAccessModal)
            }
            hasRequestedPermission &&
                    !activityRecognitionPermissionState.status.isGranted &&
                    !activityRecognitionPermissionState.status.shouldShowRationale ->{
                onAction(SmartStepAction.OnShowEnableAccessModal)
                    }
        }
        onPauseOrDispose {

        }
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
                    if (!state.motionSensorsPermissionGranted){
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAction(SmartStepAction.OnShowEnableAccessModal)
                                    }
                                    .padding(vertical = 16.dp, horizontal = 24.dp),
                                text = stringResource(R.string.stop_counting_steps_issue),
                                style = MaterialTheme.typography.bodyLargeMedium,
                                color = TextPrimary
                            )
                            HorizontalDivider(color = StrokeMain)
                        }
                    }
                    itemsIndexed(drawerItems){ index , drawerItem ->
                        if(index != 0){
                            HorizontalDivider(color = StrokeMain)
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (drawerItem.id) {
                                        R.string.step_goal -> onAction(SmartStepAction.OnStepGoalClick)
                                        R.string.personal_settings -> onAction(SmartStepAction.OnPersonSettingsClick)
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
                windowInsets =  WindowInsets.systemBars,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ){
                StepsCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    steps = state.step.formatThousands(),
                    stepsTotal = state.totalStep.toString()
                )

            }
        }

    }

    if (state.isShowStepGoal){
        StepsGoalModal(
            onStepGoalValueChanged = {
                onAction(SmartStepAction.OnStepGoalValueChanged(it))
            },
            onDismiss = {
                onAction(SmartStepAction.OnDismissStepGoal)
            },
            onSave = {
                onAction(SmartStepAction.OnStepGoalSaveClick)
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
                },
                onDismiss = {
                    onAction(SmartStepAction.OnDismissSensorsAccessModal)
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
                },
                onDismiss = {
                    onAction(SmartStepAction.OnDismissEnableAccessModal)
                }
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun SmartStepScreenPreview() {
    SmartStepTheme {
        SmartStepScreen(
            state = SmartStepState(
                step = 1234,
                isShowStepGoal = false
            ),
            onAction = {},
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        )
    }
}