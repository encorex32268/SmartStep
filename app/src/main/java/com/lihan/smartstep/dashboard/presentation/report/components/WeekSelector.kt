package com.lihan.smartstep.dashboard.presentation.report.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.IconButtonSize
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun WeekSelector(
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    currentWeek: String,
    modifier: Modifier = Modifier,
    canPrevious: Boolean = false,
    canNext: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmartStepIconButton(
            iconButtonSize = IconButtonSize.MIDDLE,
            imageVector = AppIcons.ArrowLeft,
            shape = CircleShape,
            onClick = {
                if (canPrevious){
                    onPrevious()
                }
            },
            tintColor = BackgroundWhite,
            containerColor = if (canPrevious){
                MaterialTheme.colorScheme.primary
            }else MaterialTheme.colorScheme.primaryContainer,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            text = currentWeek,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        SmartStepIconButton(
            iconButtonSize = IconButtonSize.MIDDLE,
            imageVector = AppIcons.ArrowRight,
            shape = CircleShape,
            onClick = {
                if (canNext){
                    onNext()
                }
            },
            tintColor = BackgroundWhite,
            containerColor = if (canNext){
                MaterialTheme.colorScheme.primary
            }else MaterialTheme.colorScheme.primaryContainer,
            contentDescription = null
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun WeekSelectorPreview() {
    SmartStepTheme {
        WeekSelector(
            currentWeek = "Nov 16 - Nov 22",
            onNext = {},
            onPrevious = {},
            canPrevious = true
        )
    }
}