package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.lihan.smartstep.core.presentation.components.WheelPicker
import com.lihan.smartstep.core.presentation.design_system.PrimaryButton
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.stepcount.presentation.SmartStepAction
import com.lihan.smartstep.stepcount.presentation.stepGoalItems
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary

@Composable
fun StepsGoalModal(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onStepGoalValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                WheelPicker(
                    items = stepGoalItems.reversed(),
                    initIndex = stepGoalItems.lastIndex - 2,
                    onValueChanged = onStepGoalValueChanged,
                    itemContent = { centerIndex, index , itemString ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .padding(vertical = 10.dp),
                            text = itemString,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (centerIndex  != index){
                                TextSecondary
                            }else{
                                TextPrimary
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                )
                Spacer(Modifier.height(24.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.save),
                    onClick = onSave
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

}