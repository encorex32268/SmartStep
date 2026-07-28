package com.lihan.smartstep.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.util.toFormattedTime
import com.lihan.smartstep.dashboard.presentation.components.DatePickerDialog
import com.lihan.smartstep.dashboard.presentation.components.EditStepsDialog
import kotlin.time.Clock

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier
) {
    var timeLong by remember{
        mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
    }

    val editStepsDate  = remember(timeLong){
        timeLong.toFormattedTime()
    }
    var isShowEditStepsDialog by remember {
        mutableStateOf(false)
    }
    var isShowDateDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SmartStepButton(
            text = "Edit Steps",
            onClick = {
                isShowEditStepsDialog = true
            },
            type = ButtonType.Fill
        )
    }
//    if (isShowEditStepsDialog){
//        EditStepsDialog(
//            dateString = editStepsDate,
//            stepsTextFieldState = TextFieldState(),
//            onSave = {
//
//            },
//            onDone = {},
//            onCancel = { isShowEditStepsDialog = false },
//            onDateFieldClick = {
//                isShowDateDialog = true
//            }
//        )
//        if (isShowDateDialog){
//            DatePickerDialog(
//                initialEpochMillis = timeLong,
//                onSave = { newTimeLong ->
//                    timeLong = newTimeLong
//                    isShowDateDialog = false
//                },
//                onCancel = { isShowDateDialog = false}
//            )
//        }
//    }

}


@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {

}