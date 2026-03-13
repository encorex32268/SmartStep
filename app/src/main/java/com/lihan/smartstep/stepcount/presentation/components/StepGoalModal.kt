@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.components.AdaptiveModal
import com.lihan.smartstep.core.presentation.components.WheelPicker
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.core.presentation.modifier.negativePadding
import com.lihan.smartstep.stepcount.presentation.stepGoalItems
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary

@Composable
fun StepsGoalModal(
    value: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    items: List<String> = stepGoalItems.reversed()
) {
    var currentCenterIndex by remember { mutableIntStateOf(0) }

    AdaptiveModal(
        onDismiss = onDismiss,
        dragHandle = null,
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .widthIn(max = 312.dp)
                    .heightIn(max = 376.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.modal_step_goal),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().negativePadding(16.dp)
                ){
                    Box(
                        modifier = Modifier
                            .padding(bottom = 44.dp)
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(color = BackgroundTertiary)
                            .align(Alignment.BottomStart)
                    )
                    WheelPicker(
                        items = items,
                        onValueChange = {
                            currentCenterIndex = it
                        },
                        value = value
                    )
                }
                Spacer(Modifier.height(24.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.save),
                    onClick = {
                        onSave(items[currentCenterIndex])
                    }
                )
                SmartStepTextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.cancel),
                    onClick = onCancel
                )

            }
        }
    )

}


@Preview(showBackground = true)
@Composable
private fun StepsGoalModalPreview() {
    SmartStepTheme {
        StepsGoalModal(
            value = "6000",
            onDismiss = {},
            onSave = {},
            onCancel = {}
        )
    }
}