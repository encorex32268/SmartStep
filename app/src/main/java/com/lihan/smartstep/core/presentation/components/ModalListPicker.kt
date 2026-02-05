package com.lihan.smartstep.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.SmartStepTextButton
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.BackgroundTertiary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyMediumRegular
import kotlin.math.absoluteValue

data class Picker(
    val firstText: String,
    val secondText: String,
    val selectedText: String,
    val items: List<String>
)

@Composable
fun ModalListPicker(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit,
    pickerData: Picker,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset)/2

            visibleItemsInfo.minByOrNull {
                ((it.offset) + (it.size / 2) - center).absoluteValue
            }?.index?:-1
        }
    }



    AdaptiveModal(
        modifier = modifier,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(color = BackgroundSecondary)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMediumRegular,
                    color = TextSecondary
                )
                Spacer(Modifier.height(20.dp))
                Switcher(
                    firstOption = pickerData.firstText,
                    secondOption = pickerData.secondText,
                    selectedOption = pickerData.selectedText,
                    onClick = {

                    },
                )
                Spacer(Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 176.dp),
                    state = listState,
                    flingBehavior = flingBehavior
                ) {
                    itemsIndexed(pickerData.items){ index , item ->
                        val isSelected = centerIndex == index
                        val background = if (isSelected){
                            BackgroundTertiary
                        }else Color.Transparent
                        
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(background)
                                .padding(vertical = 10.dp),
                            text = item,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                    }
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    SmartStepTextButton(
                        text = stringResource(R.string.cancel),
                        onClick = onCancelClick
                    )
                    SmartStepTextButton(
                        text = stringResource(R.string.ok),
                        onClick = onOkClick
                    )
                }
            }
        }
    )

}


@Preview(showBackground = true)
@Composable
private fun ListPickerPreview() {
    SmartStepTheme {
        ModalListPicker(
            title = stringResource(R.string.height),
            description = stringResource(R.string.used_to_calculate_distance),
            onDismiss = {},
            onOkClick = {},
            onCancelClick = {},
            pickerData = Picker(
                firstText = stringResource(R.string.cm),
                secondText = stringResource(R.string.ft_in),
                selectedText = "cm",
                items = (0..30).map {
                    "${145+it}"
                }
            )
        )
    }
}