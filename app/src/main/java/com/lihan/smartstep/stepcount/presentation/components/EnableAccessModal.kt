@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.AdaptiveModal
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyLargeMedium
import com.lihan.smartstep.ui.theme.bodyLargeRegular

@Composable
fun EnableAccessModal(
    onOpenSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdaptiveModal(
        onDismiss = {},
        isDialogLayout = false,
        modifier = modifier,
        dragHandle = null,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.enable_access_manually),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.enable_access_manually_description),
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.enable_access_steps_1),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.enable_access_steps_2),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(R.string.enable_access_steps_3),
                        style = MaterialTheme.typography.bodyLargeMedium,
                        color = TextPrimary
                    )
                }

                Spacer(Modifier.height(32.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.open_setting),
                    onClick = onOpenSettingsClick
                )
            }
        }
    )

}


@Preview(showSystemUi = true)
@Composable
private fun ExitModalPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            EnableAccessModal(
                onOpenSettingsClick = {}
            )
        }
    }
}