@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

package com.lihan.smartstep.dashboard.presentation.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.lihan.smartstep.core.presentation.design_system.bottomsheet.SmartStepModalBottomSheet
import com.lihan.smartstep.dashboard.presentation.DashboardState
import com.lihan.smartstep.dashboard.presentation.components.BackgroundAccessBottomSheet
import com.lihan.smartstep.dashboard.presentation.components.EnableAccessManuallyBottomSheet
import com.lihan.smartstep.dashboard.presentation.components.MotionSensorsAllowBottomSheet
import com.lihan.smartstep.dashboard.presentation.util.openSettings

@SuppressLint("BatteryLife")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ActivityRecognitionPermissionEffect(
    state: DashboardState,
    onShowBackgroundAccess: () -> Unit,
    onBackgroundAccessContinueClick: () -> Unit,
    onShouldShowRationale: () -> Unit,
    onNotGranted: () -> Unit,
    onPermanentlyDenied: () -> Unit,
){
    if (LocalInspectionMode.current) return //preview return

    val context = LocalContext.current
    val activity = LocalActivity.current

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.ACTIVITY_RECOGNITION,
        onPermissionResult = { isGranted ->
            if (!isGranted){
                //Get newest shouldShowRequestPermissionRationale 's value
                val isPermanentlyDenied = activity?.let {
                    !ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.ACTIVITY_RECOGNITION)
                } ?: true
                
                if (isPermanentlyDenied) {
                    onPermanentlyDenied()
                } else {
                    onNotGranted()
                }
            }else{
                onShowBackgroundAccess()
            }
        }
    )
    LaunchedEffect(permissionState) {
        val status = permissionState.status

        val isDeniedOneTime = status is PermissionStatus.Denied && status.shouldShowRationale
        val isFirstTimeRequestPermission = status is PermissionStatus.Denied && !status.shouldShowRationale
        val hasPermission = status is PermissionStatus.Granted
        when{
            isDeniedOneTime-> onShouldShowRationale()
            isFirstTimeRequestPermission -> permissionState.launchPermissionRequest()
            hasPermission-> onShowBackgroundAccess()
        }
    }
    if (state.isShowAllowAccessBottomSheet){
        SmartStepModalBottomSheet(
            onDismissRequest = {},
            dragHandle = null,
            content = {
                MotionSensorsAllowBottomSheet(
                    onAllowAccess = {
                        permissionState.launchPermissionRequest()
                    }
                )
            }
        )
    }

    if (state.isShowEnableAccessManuallyBottomSheet){
        SmartStepModalBottomSheet(
            dragHandle = null,
            onDismissRequest = {},
            content = {
                EnableAccessManuallyBottomSheet(
                    openSettings = {
                        context.openSettings()
                    }
                )
            }
        )
    }

    if (state.isShowBackgroundAccessBottomSheet){
        SmartStepModalBottomSheet(
            onDismissRequest = {},
            properties = ModalBottomSheetProperties(),
            content = {
                BackgroundAccessBottomSheet(
                    onContinue = {
                        onBackgroundAccessContinueClick()
                        context.startActivity(
                            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = "package:${context.packageName}".toUri()
                            }
                        )

                    }
                )
            }
        )
    }

}