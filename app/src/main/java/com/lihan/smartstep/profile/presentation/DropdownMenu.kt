package com.lihan.smartstep.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.R
import com.lihan.smartstep.core.presentation.design_system.ArrowsDown
import com.lihan.smartstep.core.presentation.design_system.ArrowsUp
import com.lihan.smartstep.core.presentation.design_system.CheckMark
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.StrokeMain
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextSecondary
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeRegular
import com.lihan.smartstep.ui.theme.bodySmallRegular

@Composable
fun DropdownMenu(
    title: String,
    value: String,
    selected: String,
    isExpand: Boolean,
    items: List<String>,
    onItemSelect: (String) -> Unit,
    onDropdownClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = BackgroundSecondary,
            border = BorderStroke(1.dp, StrokeMain)
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
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpand,
            onDismissRequest = onDismiss,
            containerColor = TextWhite,
            shape = RoundedCornerShape(8.dp),
            offset = DpOffset(x = 0.dp, y = 8.dp)
        ) {
            items.forEach { item ->
                val isSelected = item == selected
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 1.dp,
                            horizontal = 6.dp
                        )
                        .background(
                            color = if (isSelected) {
                                BackgroundSecondary
                            } else {
                                Color.Transparent
                            },
                            shape = RoundedCornerShape(10.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isSelected) {
                                    BackgroundSecondary
                                } else {
                                    Color.Transparent
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        text = item,
                        style = MaterialTheme.typography.bodyLargeRegular,
                        color = TextPrimary
                    )
                    if (isSelected) {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            tint = ButtonPrimary,
                            imageVector = CheckMark,
                            contentDescription = null
                        )
                    }
                }

            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun DropdownMenuPreview() {
    SmartStepTheme {
        DropdownMenu(
            title = stringResource(R.string.gender),
            value = "Female",
            selected = "Female",
            isExpand = true,
            items = listOf(
                "Female", "Male"
            ),
            onDismiss = {},
            onDropdownClick = {},
            onItemSelect = {

            }
        )
    }
}