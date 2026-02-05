@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog

@Composable
fun AdaptiveModal(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTablet = LocalConfiguration.current.screenWidthDp > 600

    if (isTablet){
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismiss
        ) {
            content()
        }
    }else{
        Dialog(
            onDismissRequest = onDismiss
        ) {
            content()
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun AdaptiveModalPreview() {

}