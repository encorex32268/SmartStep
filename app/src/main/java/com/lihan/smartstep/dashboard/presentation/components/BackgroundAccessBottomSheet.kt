package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun BackgroundAccessBottomSheet(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.background_access_recommended),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.background_access_recommended_description),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(Modifier.height(32.dp))
        SmartStepButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.button_continue),
            onClick = onContinue
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BackgroundAccessBottomSheetPreview() {
    SmartStepTheme {
        BackgroundAccessBottomSheet(
            onContinue = {}
        )
    }
}