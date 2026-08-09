package com.lihan.smartstep.dashboard.presentation.report.components

import android.hardware.hid.Report
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme
import com.lihan.smartstep.core.presentation.ui.theme.titleAccent
import com.lihan.smartstep.dashboard.presentation.report.components.ReportType.Companion.toUnit

@Composable
fun InfoCard(
    type: ReportType,
    average: String,
    modifier: Modifier = Modifier,
    value: String = ""
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = type.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = stringResource(R.string.this_week),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                modifier = Modifier.visible(value.isNotEmpty()),
                text = value,
                style = MaterialTheme.typography.titleAccent
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.daily_average,average,type.toUnit()),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.background
                )
            )
        }

    }


}

@Preview
@Composable
private fun InfoCardPreview() {
    SmartStepTheme {
        InfoCard(
            type = ReportType.Minutes,
            average = "191",
            value = "90"
        )
    }
}