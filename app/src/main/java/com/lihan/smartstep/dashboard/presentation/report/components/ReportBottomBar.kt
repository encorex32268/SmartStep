package com.lihan.smartstep.dashboard.presentation.report.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.imageVector
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.toBottomItemName

@Composable
fun ReportBottomBar(
    currentType: ReportType,
    onItemClick: (ReportType) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = StrokeMain,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
    ) {
        ReportType.entries.forEachIndexed { index, type ->
            val isSelected = currentType == type
            SegmentedButton(
                selected = isSelected,
                onClick = { onItemClick(type) },
                shape = when (index) {
                    0 -> {
                        RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    }
                    ReportType.entries.last().ordinal -> {
                        RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    }
                    else -> {
                        RoundedCornerShape(0.dp)
                    }
                },
                icon = {},
                colors = SegmentedButtonDefaults.colors(
                    activeBorderColor = Color.Transparent,
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.primary,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    inactiveContainerColor = BackgroundWhite
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        imageVector = type.imageVector,
                        contentDescription = null
                    )
                    Text(
                        text = type.toBottomItemName(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ReportBottomBarPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ){
            ReportBottomBar(
                currentType = ReportType.Steps,
                onItemClick = {}
            )
        }
    }
}