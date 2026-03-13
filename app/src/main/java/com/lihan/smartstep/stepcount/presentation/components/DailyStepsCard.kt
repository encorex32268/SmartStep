package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.modifier.negativePadding
import com.lihan.smartstep.stepcount.presentation.model.DailyStepUI
import com.lihan.smartstep.stepcount.presentation.formatThousands
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.ButtonSecondary
import com.lihan.smartstep.ui.theme.medium
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DailyStepsCard(
    average: Long,
    dailySteps: List<DailyStepUI>,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        color = ButtonPrimary,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.daily_average_steps,
                    average.formatThousands()
                    ),
                style = MaterialTheme.typography.medium,
                color = ButtonSecondary
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().negativePadding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                dailySteps.forEach { dailyStep ->
                    val percentage = remember(dailyStep) {
                        (dailyStep.steps.toLong() / dailyStep.goalSteps.toLong().toFloat())
                    }
                    DailyStep(
                        steps = dailyStep.steps.toLong().formatThousands(),
                        percentage = if (percentage.isNaN()) 0f else percentage,
                        date = dailyStep.date,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun DailyStepsCardPreview() {

    val date = getDaysOfWeek().mapIndexed { index, week ->
        DailyStepUI(
            steps = (index*2000) .toString(),
            date = week.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            goalSteps = 6000.toString(),
            time = 0
        )
    }

    DailyStepsCard(
        average = 1933,
        dailySteps = date
    )
}

fun getDaysOfWeek(firstDay: DayOfWeek = DayOfWeek.SUNDAY): List<DayOfWeek> {
    val days = DayOfWeek.entries
    val firstIndex = days.indexOf(firstDay)
    return days.subList(firstIndex, days.size) + days.subList(0, firstIndex)
}