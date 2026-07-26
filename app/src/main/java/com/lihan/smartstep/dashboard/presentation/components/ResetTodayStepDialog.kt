package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun ResetTodayStepDialog(
    onCancel: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onCancel
    ) {
        ResetTodayStep(
            onCancel = onCancel,
            onReset = onReset,
            modifier = modifier
        )
    }
}

@Composable
private fun ResetTodayStep(
    onCancel: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.reset_today_steps_information),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                SmartStepButton(
                    text = stringResource(R.string.cancel),
                    onClick = onCancel,
                    type = ButtonType.Text
                )
                SmartStepButton(
                    text = stringResource(R.string.reset),
                    onClick = onReset,
                    type = ButtonType.Text
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun ResetTodayStepDialogPreview() {
    SmartStepTheme {
        ResetTodayStep(
            onReset = {},
            onCancel = {}
        )
    }
}