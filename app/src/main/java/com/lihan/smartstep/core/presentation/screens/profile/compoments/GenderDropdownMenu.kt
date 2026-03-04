package com.lihan.smartstep.core.presentation.screens.profile.compoments

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.lihan.smartstep.core.presentation.design_system.ArrowsDown
import com.lihan.smartstep.core.presentation.design_system.CheckMark
import com.lihan.smartstep.onboarding.presentation.model.Gender
import com.lihan.smartstep.ui.theme.BackgroundSecondary
import com.lihan.smartstep.ui.theme.ButtonPrimary
import com.lihan.smartstep.ui.theme.SmartStepTheme
import com.lihan.smartstep.ui.theme.TextPrimary
import com.lihan.smartstep.ui.theme.TextWhite
import com.lihan.smartstep.ui.theme.bodyLargeRegular

@SuppressLint("ResourceType")
@Composable
fun GenderDropdownMenu(
    title: String,
    value: Gender,
    isExpand: Boolean,
    onDropdownClick: () -> Unit,
    onItemSelect: (Gender) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    items: List<Gender> = Gender.entries,
    containerColor: Color = BackgroundSecondary
) {
    val density = LocalDensity.current
    var dropdownItemWidth by remember {
        mutableStateOf(0.dp)
    }
    Box(
        modifier = modifier
    ) {
        InfoRow(
            title = title,
            value = stringResource(value.id),
            onRowClick = onDropdownClick,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged{
                    dropdownItemWidth = with(density) { it.width.toDp() }
                },
            containerColor = containerColor,
            trailingIcon = {
                Icon(
                    imageVector = ArrowsDown,
                    tint = TextPrimary,
                    contentDescription = null
                )
            }
        )
        DropdownMenu(
            modifier = Modifier.width(dropdownItemWidth),
            expanded = isExpand,
            onDismissRequest = onDismiss,
            containerColor = TextWhite,
            shape = RoundedCornerShape(8.dp),
            offset = DpOffset(x = 0.dp, y = 8.dp)
        ) {
            items.forEach { item ->
                val isSelected = item == value
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onItemSelect(item)
                            }
                        )
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
                        text = stringResource(item.id),
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
private fun GenderDropdownMenuPreview() {
    SmartStepTheme {
        GenderDropdownMenu(
            title = "Gender",
            value = Gender.FEMALE,
            isExpand = false,
            onDropdownClick = {},
            onDismiss = {},
            onItemSelect = {

            }
        )
    }
}