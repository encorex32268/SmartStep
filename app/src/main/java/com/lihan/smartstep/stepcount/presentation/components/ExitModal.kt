@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.lihan.smartstep.core.presentation.design_system.PowerTurnOn
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyLargeRegular

@Composable
fun ExitModal(
    onOkClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdaptiveModal(
        onDismiss = onDismiss,
        isDialogLayout = true,
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(34.dp),
                    imageVector = PowerTurnOn,
                    contentDescription = null,
                    tint = TextPrimary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.not_run_in_the_background),
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.ok),
                    onClick = onOkClick
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
            ExitModal(
                onOkClick = {},
                onDismiss = {}
            )
        }
    }
}