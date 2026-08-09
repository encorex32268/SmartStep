package com.lihan.smartstep.dashboard.presentation.report.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.AppIcons
import com.lihan.smartstep.core.presentation.design_system.buttons.IconButtonSize
import com.lihan.smartstep.core.presentation.design_system.buttons.SmartStepIconButton
import com.lihan.smartstep.core.presentation.ui.theme.BackgroundWhite
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.StrokeMain
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.toUnit

@Composable
fun DailyInfoCard(
    dayOfWeek: String,
    status: DailyInfoStatus,
    type: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String = "",
    value: String = ""
) {
    when(status){
        DailyInfoStatus.Done,
        DailyInfoStatus.Doing -> {
            Surface(
                modifier = modifier,
                onClick = onItemClick,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (status == DailyInfoStatus.Doing) MaterialTheme.colorScheme.primary else StrokeMain
                ),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = dayOfWeek,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (status == DailyInfoStatus.Doing){
                                    MaterialTheme.colorScheme.primary
                                }else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        SmartStepIconButton(
                            imageVector = if (status == DailyInfoStatus.Done) AppIcons.Check else AppIcons.TimeClock,
                            iconButtonSize = IconButtonSize.EXTRA_SMALL,
                            containerColor = MaterialTheme.colorScheme.surface,
                            tintColor = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            contentDescription = null
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = value.ifEmpty { stringResource(R.string.zero) },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (status == DailyInfoStatus.Doing){
                                    MaterialTheme.colorScheme.primary
                                }else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                }
            }
        }
        DailyInfoStatus.NotYet -> {
            Surface(
                modifier = modifier,
                onClick = onItemClick,
                border = BorderStroke(
                    width = 1.dp,
                    color = StrokeMain
                ),
                shape = RoundedCornerShape(12.dp),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = dayOfWeek,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        SmartStepIconButton(
                            imageVector = AppIcons.Minus,
                            iconButtonSize = IconButtonSize.ULTRA_SMALL,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            tintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = CircleShape,
                            contentDescription = null
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.zero),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = description.ifEmpty { stringResource(R.string.no_data) },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DailyInfoCardPreview() {
    SmartStepTheme {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DailyInfoCard(
                dayOfWeek = "Thursday",
                value = "555",
                status = DailyInfoStatus.Done,
                type = ReportType.Calories.toUnit(),
                onItemClick = {},
            )
            DailyInfoCard(
                dayOfWeek = "Thursday",
                value = "555",
                status = DailyInfoStatus.Doing,
                type = ReportType.Steps.toUnit(),
                onItemClick = {},
                description = "Goal: 6000 steps"
            )
            DailyInfoCard(
                dayOfWeek = "Thursday",
                value = "",
                status = DailyInfoStatus.NotYet,
                type = ReportType.Steps.toUnit(),
                onItemClick = {},
                description = "Goal: 6000 steps"
            )

        }
    }
}