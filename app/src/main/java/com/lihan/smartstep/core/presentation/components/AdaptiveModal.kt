@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.ui.theme.BackgroundSecondary

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AdaptiveModal(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isDialogLayout: Boolean = false,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
) {
    val isTablet = LocalConfiguration.current.screenWidthDp > 600

    if (!isTablet && !isDialogLayout){
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismiss,
            containerColor = BackgroundSecondary,
            dragHandle = dragHandle
        ) {
            content()
        }
    }else{
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = modifier
                    .background(BackgroundSecondary, RoundedCornerShape(28.dp))
            ) {
                content()
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun AdaptiveModalPreview() {

}