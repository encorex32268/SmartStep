package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun ExitInformationDialog(
    onDismissRequest: () -> Unit,
    onOkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        ExitInformation(
            modifier = modifier,
            onOkClick = onOkClick
        )
    }
}


@Composable
private fun ExitInformation(
    onOkClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = AppIcons.Start,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.exit_information),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(Modifier.height(24.dp))
            SmartStepButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.ok),
                onClick = onOkClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExitInformationDialogPreview() {
    SmartStepTheme {
        ExitInformation(
            onOkClick = {}
        )
    }
}