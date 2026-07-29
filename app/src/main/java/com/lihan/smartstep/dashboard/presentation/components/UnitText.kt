package com.lihan.smartstep.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.ui.theme.SmartStepTheme

@Composable
fun UnitText(
    text: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier.align(Alignment.Bottom),
            text = unit,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surface
        )
    }

}


@Preview(showBackground = true, backgroundColor = 0xFF)
@Composable
private fun UnitTextPreview() {
    SmartStepTheme {
        UnitText(
            text = "3.2",
            unit = "km"
        )
    }
}