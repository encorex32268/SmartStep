package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.SettingsField
import com.lihan.smartstep.core.presentation.components.SettingsTextField
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.util.toFormattedTime

@Composable
fun EditStepsDialog(
    dateTime: Long,
    stepsTextFieldState: TextFieldState,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDateFieldClick: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onCancel
    ) {
        EditSteps(
            modifier = modifier,
            dateTime = dateTime,
            stepsTextFieldState = stepsTextFieldState,
            onSave = onSave,
            onCancel = onCancel,
            onDone = onDone,
            onDateFieldClick = onDateFieldClick
        )
    }

}

@Composable
private fun EditSteps(
    modifier: Modifier = Modifier,
    dateTime: Long,
    stepsTextFieldState: TextFieldState,
    onCancel: () -> Unit,
    onDateFieldClick: () -> Unit,
    onSave: () -> Unit,
    onDone: () -> Unit
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
                value = dateTime.toFormattedTime(),
                onFieldClick = onDateFieldClick
            )
            Spacer(Modifier.height(8.dp))
            SettingsTextField(
                title = stringResource(R.string.steps),
                value = stepsTextFieldState,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onDone = onDone
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
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
            onCancel = {},
            onDone = {},
            onDateFieldClick = {},
            stepsTextFieldState = TextFieldState(),
            dateTime = kotlin.time.Clock.System.now().toEpochMilliseconds()
        )
    }
}