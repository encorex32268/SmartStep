package com.lihan.smartstep.stepcount.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lihan.smartstep.ui.theme.ButtonSecondary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeRegular
import com.lihan.smartstep.ui.theme.bodyMediumMedium

@Composable
fun CounterElement(
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.alignByBaseline(),
            text = value,
            style = MaterialTheme.typography.bodyMediumMedium.copy(
                color = TextWhite,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            modifier = Modifier.alignByBaseline(),
            text = unit,
            style = MaterialTheme.typography.bodyLargeRegular.copy(
                color = ButtonSecondary,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

        )
    }

}


@Preview(showBackground = true)
@Composable
private fun CounterElementPreview() {
    SmartStepTheme {
        CounterElement(
            value = "3.2",
            unit = "km"
        )
    }
}