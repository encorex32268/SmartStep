
package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lihan.smartstep.ui.theme.BackgroundSecondary


@ExperimentalMaterial3Api
@Composable
fun AdaptiveModal(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isDialogLayout: Boolean = false,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(
        shouldDismissOnBackPress = false,
        shouldDismissOnClickOutside = false
    ),
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    )
) {
    val isTablet = LocalConfiguration.current.screenWidthDp > 600

    if (!isTablet && !isDialogLayout){
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismiss,
            properties = modalBottomSheetProperties,
            sheetGesturesEnabled = false,
            containerColor = BackgroundSecondary,
            dragHandle = dragHandle
        ) {
            content()
        }
    }else{
        Dialog(
            onDismissRequest = onDismiss,
            properties = dialogProperties
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