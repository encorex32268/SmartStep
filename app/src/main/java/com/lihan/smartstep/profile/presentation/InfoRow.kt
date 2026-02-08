package com.lihan.smartstep.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.design_system.ArrowsDown
import com.lihan.smartstep.core.presentation.design_system.ArrowsUp
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.bodyLargeRegular
import com.lihan.smartstep.ui.theme.bodySmallRegular

@Composable
fun InfoRow(
    title: String,
    value: String,
    onRowClick: () -> Unit,
    modifier: Modifier = Modifier,
    isExpand: Boolean = false
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = BackgroundSecondary,
        border = BorderStroke(1.dp, StrokeMain),
        onClick = onRowClick
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmallRegular,
                    color = TextSecondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLargeRegular,
                    color = TextPrimary
                )
            }

            Icon(
                imageVector = if (isExpand) {
                    ArrowsUp
                } else {
                    ArrowsDown
                },
                tint = TextPrimary,
                contentDescription = null
            )

        }

    }

}
