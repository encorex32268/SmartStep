package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.SettingsField
import com.lihan.smartstep.core.presentation.components.SingleValueWheelPicker
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import kotlin.time.Clock

@Composable
fun EditStepsDialog(
    onSave: (Long) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {


}

@Composable
private fun EditSteps(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,

){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.edit_steps),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.edit_steps_information),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(Modifier.height(24.dp))

            SettingsField(
                title = stringResource(R.string.date),
                value = "2025/11/30",
                onFieldClick = {

                }
            )
            Spacer(Modifier.height(8.dp))
            SettingsField(
                title = stringResource(R.string.date),
                value = "2025/11/30",
                onFieldClick = {

                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                SmartStepButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancel,
                    type = ButtonType.Text
                )
                SmartStepButton(
                    text = stringResource(R.string.save),
                    onClick = onSave,
                    type = ButtonType.Text
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun DatePickerDialogPreview() {
    SmartStepTheme {
        EditSteps(
            onSave = {},
            onCancel = {}
        )
    }
}