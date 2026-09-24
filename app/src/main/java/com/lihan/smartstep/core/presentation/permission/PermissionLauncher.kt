package com.lihan.smartstep.core.presentation.permission

import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Modern AndroidX native permission requester hook.
 *
 * @param onGranted Invoked when permission is already granted or newly accepted.
 * @param onDenied Invoked when permission is denied.
 *                 [isPermanentlyDenied] is true if the user checked "Don't ask again"
 *                 or denied multiple times (rationale is false).
 * @return A lambda accepting the permission string (e.g. Manifest.permission.CAMERA).
 */
@Composable
fun rememberPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: (isPermanentlyDenied: Boolean) -> Unit = {}
): (String) -> Unit {
    if (LocalInspectionMode.current) return {}

    val context = LocalContext.current
    val activity = LocalActivity.current

    var targetPermission by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            val permission = targetPermission
            if (activity != null && permission != null) {
                // When rationale is false after denial, the permission is permanently denied.
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
                onDenied(!shouldShowRationale)
            } else {
                onDenied(false)
            }
        }
    }

    return { permission ->
        targetPermission = permission
        val currentStatus = ContextCompat.checkSelfPermission(context, permission)
        if (currentStatus == PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else {
            launcher.launch(permission)
        }
    }
}
