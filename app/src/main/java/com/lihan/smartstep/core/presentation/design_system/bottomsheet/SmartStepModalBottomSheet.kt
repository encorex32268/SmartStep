@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.core.presentation.design_system.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmartStepModalBottomSheet(
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
    modifier: Modifier = Modifier,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(
        shouldDismissOnBackPress = false,
        shouldDismissOnClickOutside = false
    )
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = false,
        shape = RoundedCornerShape(topStart = 24.dp , topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = dragHandle,
        content = content,
        properties = properties
    )
}
