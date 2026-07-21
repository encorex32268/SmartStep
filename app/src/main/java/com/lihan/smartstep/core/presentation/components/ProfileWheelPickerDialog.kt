package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.smartstep.R
import com.lihan.smartstep.core.data.model.HeightUnit
import com.lihan.smartstep.core.presentation.design_system.buttons.ButtonType
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepButton
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain


@Composable
fun ProfileWheelPickerDialog(
    title: String,
    description: String,
    selectOption: String,
    options: List<String>,
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    onOptionClick: (Int) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
){
    Dialog(
        onDismissRequest = onCancelClick
    ) {
        ProfileWheelPicker(
            title = title,
            description = description,
            selectOption = selectOption,
            options = options,
            onCancelClick = onCancelClick,
            onOkClick = onOkClick,
            onOptionClick = onOptionClick,
            content = content,
            modifier = modifier
        )
    }
}


@Composable
private fun ProfileWheelPicker(
    title: String,
    description: String,
    selectOption: String,
    options: List<String>,
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    onOptionClick: (Int) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(28.dp)
            )
            .width(328.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp , bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .width(280.dp)
                .fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.secondary,
                        activeContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledActiveContainerColor = StrokeMain,
                        disabledInactiveContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = {
                        onOptionClick(index)
                    },
                    selected = selectOption == label,
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                )
            }
        }
        Spacer(Modifier.height(28.dp))
        content()
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            SmartStepButton(
                text = stringResource(R.string.cancel),
                onClick = onCancelClick,
                type = ButtonType.Text
            )
            SmartStepButton(
                text = stringResource(R.string.Ok),
                onClick = onOkClick,
                type = ButtonType.Text
            )
        }
    }

}





@Preview
@Composable
private fun ProfileWheelPickerDialogPreview() {
    SmartStepTheme {
        ProfileWheelPicker(
            title = "Title",
            description = "description",
            selectOption = HeightUnit.entries.first().name.lowercase(),
            options = HeightUnit.lowercaseUnits,
            onOkClick = {},
            onCancelClick = {},
            onOptionClick = {},
            content = {}
        )
    }
}