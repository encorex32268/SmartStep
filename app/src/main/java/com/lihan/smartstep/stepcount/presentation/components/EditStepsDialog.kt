@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.AdaptiveModal
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.core.presentation.screens.profile.compoments.SmartStepTextField
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyMediumRegular

@Composable
fun EditStepsDialog(
    dateTextFieldState: TextFieldState,
    stepsTextState: TextFieldState,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdaptiveModal(
        modifier = modifier,
        isDialogLayout = true,
        onDismiss = onCancelClick,
        dialogProperties = DialogProperties(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_steps),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.edit_steps_description),
                    style = MaterialTheme.typography.bodyMediumRegular,
                    color = TextSecondary
                )
                Spacer(Modifier.height(24.dp))

                SmartStepTextField(
                    title = stringResource(R.string.date),
                    textState = dateTextFieldState,
                    onClick = onDateClick,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrows_down),
                            contentDescription = "arrow down"
                        )
                    }
                )
                Spacer(Modifier.height(8.dp))
                SmartStepTextField(
                    title = stringResource(R.string.steps),
                    textState = stepsTextState,
                    onClick = {}
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    SmartStepTextButton(
                        text = stringResource(R.string.cancel),
                        onClick = onCancelClick
                    )
                    Spacer(Modifier.width(4.dp))
                    SmartStepTextButton(
                        text = stringResource(R.string.save),
                        onClick = onSaveClick
                    )
                }
            }
        }
    )

}





@Preview(showBackground = true)
@Composable
private fun EditStepsDialogPreview() {
    SmartStepTheme {
        EditStepsDialog(
            dateTextFieldState = TextFieldState("2026/03/03"),
            stepsTextState = TextFieldState("23000"),
            onSaveClick = {},
            onCancelClick = {},
            onDateClick = {}
        )
    }
}