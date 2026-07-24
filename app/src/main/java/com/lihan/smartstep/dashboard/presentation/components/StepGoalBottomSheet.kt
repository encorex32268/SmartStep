package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.SingleValueWheelPicker
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun StepGoalBottomSheet(
    value: String,
    items: List<String>,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp
) {
    var selectedString by rememberSaveable(value){
        mutableStateOf(value)
    }
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.step_goal),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .offset(y = itemHeight * 2)
                        .background(
                            color =  MaterialTheme.colorScheme.surfaceVariant
                        )
                )
                SingleValueWheelPicker(
                    value = value,
                    items = items,
                    onValueChange = {
                        selectedString = it
                    }
                )

            }
            Spacer(Modifier.height(24.dp))
            SmartStepButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save),
                onClick = {
                    onSave(selectedString)
                }
            )
            SmartStepButton(
                modifier = Modifier.fillMaxWidth(),
                type = ButtonType.Text,
                text = stringResource(R.string.cancel),
                onClick = onCancel
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun StepGoalBottomSheetPreview() {
    SmartStepTheme {
        StepGoalBottomSheet(
            value = "5",
            items = (0..20).map { it.toString() },
            onSave = {},
            onCancel = {}
        )
    }
}